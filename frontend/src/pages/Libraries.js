import { useEffect, useState } from "react";
import api from "../api/api";
import Sidebar from "../components/Sidebar";
import {
    TextField,
    Button,
    Box,
    Typography,
    Grid,
    Card,
    CardContent,
    Avatar,
    Paper,
    Snackbar,
    Alert,
    CircularProgress,
    Container
} from "@mui/material";
import {
    LibraryBooks as LibraryIcon,
    AddCircle as AddIcon,
    AutoStories as BookIcon
} from "@mui/icons-material";

export default function Libraries() {
    const [libraries, setLibraries] = useState([]);
    const [name, setName] = useState("");
    const [loading, setLoading] = useState(true);

    // Bildirim (Snackbar) state'i
    const [notification, setNotification] = useState({ open: false, message: "", severity: "success" });

    const isLoggedIn = Boolean(localStorage.getItem("accessToken"));

    const handleCloseNotification = () => {
        setNotification({ ...notification, open: false });
    };

    const showNotification = (message, severity) => {
        setNotification({ open: true, message, severity });
    };

    const loadLibraries = async () => {
        setLoading(true);
        try {
            const res = await api.get("/v1/library/list");
            setLibraries(res.data);
        } catch (err) {
            console.error("Kütüphaneler yüklenirken hata:", err);
            showNotification("Kütüphaneler yüklenemedi.", "error");
        } finally {
            setLoading(false);
        }
    };

    const createLibrary = async (e) => {
        e.preventDefault();
        if (!name.trim()) {
            showNotification("⚠️ Kategori adı boş olamaz!", "warning");
            return;
        }

        try {
            await api.post(`/v1/library/create?categoryName=${name}`);
            showNotification("📌 Yeni kütüphane başarıyla oluşturuldu ✔", "success");
            setName("");
            loadLibraries();
        } catch (err) {
            console.error(err);
            showNotification("❌ Yetkiniz yok veya bir hata oluştu.", "error");
        }
    };

    useEffect(() => {
        loadLibraries();
    }, []);

    return (
        <Box sx={{ display: "flex", backgroundColor: "#f4f6f8", minHeight: "100vh" }}>
            <Sidebar />

            <Container maxWidth="lg" sx={{ py: 4 }}>
                {/* --- BAŞLIK ALANI --- */}
                <Box sx={{ display: "flex", alignItems: "center", mb: 4, gap: 2 }}>
                    <Avatar sx={{ bgcolor: "primary.main", width: 56, height: 56 }}>
                        <LibraryIcon fontSize="large" />
                    </Avatar>
                    <Box>
                        <Typography variant="h4" fontWeight="bold" color="text.primary">
                            Kütüphaneler
                        </Typography>
                        <Typography variant="body1" color="text.secondary">
                            Mevcut kitap kategorilerini yönetin ve yenilerini ekleyin.
                        </Typography>
                    </Box>
                </Box>

                {/* --- EKLEME FORMU (Sadece giriş yapmışsa) --- */}
                {isLoggedIn && (
                    <Paper
                        elevation={3}
                        component="form"
                        onSubmit={createLibrary}
                        sx={{
                            p: 3,
                            mb: 5,
                            borderRadius: 2,
                            display: "flex",
                            alignItems: "center",
                            gap: 2,
                            background: "linear-gradient(to right, #ffffff, #f9f9f9)"
                        }}
                    >
                        <TextField
                            label="Yeni Kütüphane Adı"
                            variant="outlined"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            fullWidth
                            size="small"
                            placeholder="Örn: Bilim Kurgu, Tarih..."
                        />
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            startIcon={<AddIcon />}
                            sx={{ px: 4, py: 1, fontWeight: "bold" }}
                        >
                            Ekle
                        </Button>
                    </Paper>
                )}

                {/* --- LİSTE ALANI --- */}
                <Box>
                    <Typography variant="h5" sx={{ mb: 3, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 1 }}>
                        <BookIcon color="action" />
                        Mevcut Kütüphaneler
                    </Typography>

                    {loading ? (
                        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
                            <CircularProgress />
                        </Box>
                    ) : libraries.length > 0 ? (
                        <Grid container spacing={3}>
                            {libraries.map((library) => (
                                <Grid item xs={12} sm={6} md={4} key={library.id}>
                                    <Card
                                        elevation={2}
                                        sx={{
                                            borderRadius: 3,
                                            transition: "0.3s",
                                            "&:hover": {
                                                transform: "translateY(-5px)",
                                                boxShadow: 6,
                                                cursor: "pointer"
                                            },
                                        }}
                                    >
                                        <CardContent sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                                            <Avatar
                                                sx={{
                                                    bgcolor: "secondary.light",
                                                    color: "secondary.contrastText"
                                                }}
                                            >
                                                {library.categoryName.charAt(0).toUpperCase()}
                                            </Avatar>
                                            <Box>
                                                <Typography variant="h6" component="div" fontWeight="bold">
                                                    {library.categoryName}
                                                </Typography>
                                                <Typography variant="caption" color="text.secondary">
                                                    ID: {library.id}
                                                </Typography>
                                            </Box>
                                        </CardContent>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    ) : (
                        <Paper sx={{ p: 4, textAlign: "center", bgcolor: "#fff0f0" }}>
                            <Typography variant="h6" color="error">
                                📭 Henüz kütüphane bulunmuyor.
                            </Typography>
                        </Paper>
                    )}
                </Box>
            </Container>

            {/* --- BİLDİRİM (TOAST) --- */}
            <Snackbar
                open={notification.open}
                autoHideDuration={4000}
                onClose={handleCloseNotification}
                anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            >
                <Alert onClose={handleCloseNotification} severity={notification.severity} variant="filled" sx={{ width: "100%" }}>
                    {notification.message}
                </Alert>
            </Snackbar>
        </Box>
    );
}