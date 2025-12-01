import { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";
import { TextField, Button, Box, Typography } from "@mui/material";

export default function AdminLogin() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const login = async () => {
        try {
            const res = await api.post("/v1/auth/login", { username, password });

            localStorage.setItem("accessToken", res.data.token);
            localStorage.setItem("refreshToken", res.data.refreshToken);
            localStorage.setItem("username", username);

            navigate("/admin");
        } catch {
            alert("❌ Kullanıcı adı veya şifre hatalı!");
        }
    };

    return (
        <Box
            sx={{
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#f5f7fa",
            }}
        >
            <Box
                sx={{
                    width: 380,
                    padding: "40px",
                    borderRadius: "12px",
                    backgroundColor: "#ffffff",
                    border: "1px solid #e0e0e0",
                    textAlign: "center",
                    boxShadow: "0px 6px 18px rgba(0,0,0,0.08)"
                }}
            >
                <Typography variant="h5"
                            sx={{
                                color: "#222",
                                fontWeight: 700,
                                mb: 3
                            }}>
                    🔐 Admin Giriş
                </Typography>

                <TextField
                    fullWidth
                    label="Kullanıcı Adı"
                    variant="outlined"
                    onChange={(e) => setUsername(e.target.value)}
                    sx={{
                        mb: 2,
                        "& .MuiOutlinedInput-root": {
                            "&.Mui-focused fieldset": { borderColor: "#4b6bfb" }
                        }
                    }}
                />

                <TextField
                    fullWidth
                    type="password"
                    label="Şifre"
                    variant="outlined"
                    onChange={(e) => setPassword(e.target.value)}
                    sx={{
                        mb: 3,
                        "& .MuiOutlinedInput-root": {
                            "&.Mui-focused fieldset": { borderColor: "#4b6bfb" }
                        }
                    }}
                />

                <Button
                    fullWidth
                    variant="contained"
                    sx={{
                        background: "#4b6bfb",
                        fontWeight: "bold",
                        py: 1.4,
                        borderRadius: "10px",
                        color: "white",
                        textTransform: "none",
                        fontSize: "16px",
                        transition: "0.3s",
                        "&:hover": {
                            background: "#3a52d1",
                            transform: "scale(1.02)"
                        }
                    }}
                    onClick={login}
                >
                    Giriş Yap
                </Button>
            </Box>
        </Box>
    );
}
