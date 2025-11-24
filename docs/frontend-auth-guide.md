# Frontend auth integration

The auth-service issues both an access token and a refresh token. The React snippet below stores both tokens (plus the username) and automatically refreshes the access token when a request fails with 401.

## What the backend expects
- `/v1/auth/login` returns `{ "accessToken", "refreshToken" }` after validating the credentials against admin-service.
- `/v1/auth/refresh` expects a JSON body `{ "username", "refreshToken" }` and returns a new access token.
- `/v1/auth/logout` reads the `Authorization: Bearer <access>` header, blacklists the access token until it expires, and revokes the stored refresh token for that user.

The default expirations are `jwt.accessTtlMs=9000000` (access token) and `jwt.refreshTtlMs=604800000` (refresh token, stored in Redis).

## Example axios setup with automatic refresh
```ts
import axios from "axios";

const api = axios.create({ baseURL: "http://localhost:8889" });

function getSession() {
  const raw = localStorage.getItem("session");
  return raw ? JSON.parse(raw) : null; // { username, accessToken, refreshToken }
}

api.interceptors.request.use((config) => {
  const session = getSession();
  if (session?.accessToken) {
    config.headers.Authorization = `Bearer ${session.accessToken}`;
  }
  return config;
});

let isRefreshing = false;
let queue = [] as (() => void)[];

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (error.response?.status !== 401 || original._retry) throw error;

    const session = getSession();
    if (!session?.refreshToken || !session?.username) throw error;

    if (isRefreshing) {
      await new Promise<void>((resolve) => queue.push(resolve));
      return api(original);
    }

    isRefreshing = true;
    original._retry = true;

    try {
      const { data } = await api.post("/v1/auth/refresh", {
        username: session.username,
        refreshToken: session.refreshToken,
      });

      const updated = { ...session, accessToken: data.accessToken };
      localStorage.setItem("session", JSON.stringify(updated));
      queue.forEach((resolve) => resolve());
      queue = [];
      return api(original);
    } catch (refreshErr) {
      localStorage.removeItem("session");
      throw refreshErr;
    } finally {
      isRefreshing = false;
    }
  }
);

export default api;
```

## Handling login & logout in React
```tsx
async function handleLogin(username: string, password: string) {
  const { data } = await api.post("/v1/auth/login", { username, password });
  localStorage.setItem(
    "session",
    JSON.stringify({ username, accessToken: data.accessToken, refreshToken: data.refreshToken })
  );
}

async function handleLogout() {
  const session = getSession();
  if (session?.accessToken) {
    await api.post(
      "/v1/auth/logout",
      {},
      { headers: { Authorization: `Bearer ${session.accessToken}` } }
    );
  }
  localStorage.removeItem("session");
}
```

This approach keeps the frontend aligned with the backend contract: the access token is sent on each request, refresh requests include both username and refresh token, and logout revokes both tokens server-side.
