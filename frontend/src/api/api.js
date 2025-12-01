import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8889"
});

const publicEndpoints = [
    "/v1/auth/login",
    "/v1/auth/refresh",
    "/v1/book/list",
    "/v1/book/isbn",
    "/v1/book/book",
    "/v1/book/search",
    "/v1/library/removeFromLibraries",
];

api.interceptors.request.use(config => {
    const token = localStorage.getItem("accessToken");

    // Public endpointse token ekleme
    const isPublic = publicEndpoints.some(ep => config.url.startsWith(ep));

    if (!isPublic && token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});


// Token süresi dolarsa otomatik yenileme
let isRefreshing = false;
let refreshQueue = [];

api.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        // Eğer 401 ve refresh henüz denenmediyse
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            if (!isRefreshing) {
                isRefreshing = true;

                const refreshToken = localStorage.getItem("refreshToken");

                try {
                    const res = await axios.post("http://localhost:8889/v1/auth/refresh", {
                        refreshToken: refreshToken,
                        username: localStorage.getItem("username")
                    });

                    const newAccess = res.data.accessToken;
                    localStorage.setItem("accessToken", newAccess);

                    isRefreshing = false;
                    refreshQueue.forEach(cb => cb(newAccess));
                    refreshQueue = [];

                    return api(originalRequest);

                } catch (err) {
                    isRefreshing = false;
                    refreshQueue = [];
                    localStorage.clear();
                    window.location.href = "/";
                    return Promise.reject(err);
                }
            }

            return new Promise(resolve => {
                refreshQueue.push(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    resolve(api(originalRequest));
                });
            });
        }

        return Promise.reject(error);
    }
);

export default api;
