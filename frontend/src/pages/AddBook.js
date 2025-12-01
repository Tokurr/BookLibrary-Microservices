import { useState, useEffect } from "react";
import api from "../api/api";
import Sidebar from "../components/Sidebar";
import {
    TextField, Button, Box, Grid, CircularProgress, Paper, Typography,
    Divider, InputAdornment, IconButton, Snackbar, Alert, Stack,
    MenuItem, Select, FormControl, InputLabel
} from "@mui/material";
import {
    Book, Person, Numbers, Category, CalendarToday, Business, Description,
    Place, MeetingRoom, GridOn, FolderOpen, CloudUpload, Delete, AccountBalance
} from "@mui/icons-material";

export default function AddBook() {
    // --- STATE YÖNETİMİ ---
    const [form, setForm] = useState({
        title: "", author: "", isbn: "", category: "",
        bookYear: "", pressName: "", description: ""
    });

    const [location, setLocation] = useState({
        floor: "", room: "", shelf: "", section: ""
    });

    // Kütüphane Seçimi için State
    const [libraries, setLibraries] = useState([]);
    const [selectedLibraryId, setSelectedLibraryId] = useState("");

    const [image, setImage] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [loading, setLoading] = useState(false);

    const [snackbar, setSnackbar] = useState({ open: false, message: "", severity: "success" });

    // --- SAYFA YÜKLENDİĞİNDE KÜTÜPHANELERİ ÇEK ---
    useEffect(() => {
        const fetchLibraries = async () => {
            try {
                const response = await api.get("/v1/library/list");
                setLibraries(response.data);
            } catch (error) {
                console.error("Kütüphaneler yüklenirken hata oluştu:", error);
                setSnackbar({ open: true, message: "Kütüphaneler yüklenemedi!", severity: "error" });
            }
        };
        fetchLibraries();
    }, []);

    // --- HANDLERS ---
    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });
    const handleLocationChange = (e) => setLocation({ ...location, [e.target.name]: e.target.value });

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImage(file);
            setImagePreview(URL.createObjectURL(file));
        }
    };

    const removeImage = () => {
        setImage(null);
        setImagePreview(null);
    };

    const handleSubmit = async () => {
        // Validasyonlar
        if (!form.title || !form.author || !form.isbn) {
            setSnackbar({ open: true, message: "⚠️ Başlık, Yazar ve ISBN alanları zorunludur!", severity: "warning" });
            return;
        }

        if (!selectedLibraryId) {
            setSnackbar({ open: true, message: "⚠️ Lütfen kitabı eklemek için bir kütüphane seçiniz.", severity: "warning" });
            return;
        }

        setLoading(true);
        try {
            // ADIM 1: Kitabı Oluştur
            const requestBody = { ...form, locationDto: location };
            const data = new FormData();
            data.append("book", new Blob([JSON.stringify(requestBody)], { type: "application/json" }));

            if (image) {
                data.append("coverImage", image);
            }

            await api.post("/v1/book/create", data, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            // ADIM 2: Kitabı Kütüphaneye Ata (LibraryController /update endpointi)
            // Backend AddBookRequest (id: libraryId, isbn: bookIsbn) bekliyor
            await api.put("/v1/library/update", {
                id: selectedLibraryId,
                isbn: form.isbn
            });

            setSnackbar({ open: true, message: "📚 Kitap oluşturuldu ve kütüphaneye eklendi!", severity: "success" });

            // Formu sıfırla
            setForm({ title: "", author: "", isbn: "", category: "", bookYear: "", pressName: "", description: "" });
            setLocation({ floor: "", room: "", shelf: "", section: "" });
            setSelectedLibraryId(""); // Seçimi sıfırla
            removeImage();

        } catch (err) {
            console.error(err);
            // Hata yönetimi: Belki kitap oluştu ama kütüphaneye eklenemedi?
            // Basitlik adına genel hata veriyoruz.
            setSnackbar({ open: true, message: "❌ Bir hata oluştu. ISBN benzersiz olmalı.", severity: "error" });
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ display: "flex", minHeight: "100vh", backgroundColor: "#f4f6f8" }}>
            <Sidebar />

            <Box component="main" sx={{ flexGrow: 1, p: 4, display: "flex", justifyContent: "center" }}>
                <Paper elevation={3} sx={{ p: 4, width: "100%", maxWidth: 900, borderRadius: 3 }}>

                    {/* --- Üst Başlık --- */}
                    <Box sx={{ mb: 4, textAlign: "center" }}>
                        <Typography variant="h4" component="h1" fontWeight="bold" color="primary">
                            📖 Yeni Kitap ve Envanter Kaydı
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Kitabı oluşturun ve ilgili kütüphaneye atayın.
                        </Typography>
                    </Box>

                    <Grid container spacing={4}>
                        {/* --- SOL KOLON: Kitap Bilgileri --- */}
                        <Grid item xs={12} md={7}>
                            <Typography variant="h6" sx={{ mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                                <Description color="action" /> Temel Bilgiler
                            </Typography>

                            <Stack spacing={2}>
                                <TextField
                                    label="Kitap Adı"
                                    name="title"
                                    value={form.title}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                    InputProps={{ startAdornment: (<InputAdornment position="start"><Book /></InputAdornment>) }}
                                />

                                <Grid container spacing={2}>
                                    <Grid item xs={12} sm={6}>
                                        <TextField
                                            label="Yazar"
                                            name="author"
                                            value={form.author}
                                            onChange={handleChange}
                                            fullWidth
                                            required
                                            InputProps={{ startAdornment: (<InputAdornment position="start"><Person /></InputAdornment>) }}
                                        />
                                    </Grid>
                                    <Grid item xs={12} sm={6}>
                                        <TextField
                                            label="Yayınevi"
                                            name="pressName"
                                            value={form.pressName}
                                            onChange={handleChange}
                                            fullWidth
                                            InputProps={{ startAdornment: (<InputAdornment position="start"><Business /></InputAdornment>) }}
                                        />
                                    </Grid>
                                </Grid>

                                <Grid container spacing={2}>
                                    <Grid item xs={6}>
                                        <TextField
                                            label="Yayın Yılı"
                                            name="bookYear"
                                            value={form.bookYear}
                                            onChange={handleChange}
                                            fullWidth
                                            type="number"
                                            InputProps={{ startAdornment: (<InputAdornment position="start"><CalendarToday /></InputAdornment>) }}
                                        />
                                    </Grid>
                                    <Grid item xs={6}>
                                        <TextField
                                            label="ISBN"
                                            name="isbn"
                                            value={form.isbn}
                                            onChange={handleChange}
                                            fullWidth
                                            required
                                            placeholder="Benzersiz barkod no"
                                            InputProps={{ startAdornment: (<InputAdornment position="start"><Numbers /></InputAdornment>) }}
                                        />
                                    </Grid>
                                </Grid>

                                <TextField
                                    label="Kategori"
                                    name="category"
                                    value={form.category}
                                    onChange={handleChange}
                                    fullWidth
                                    InputProps={{ startAdornment: (<InputAdornment position="start"><Category /></InputAdornment>) }}
                                />

                                <TextField
                                    label="Açıklama / Özet"
                                    name="description"
                                    value={form.description}
                                    onChange={handleChange}
                                    multiline
                                    rows={3}
                                    fullWidth
                                />
                            </Stack>
                        </Grid>

                        {/* --- SAĞ KOLON: Kütüphane & Konum & Resim --- */}
                        <Grid item xs={12} md={5}>

                            {/* --- YENİ: Kütüphane Seçimi --- */}
                            <Box sx={{ mb: 4 }}>
                                <Typography variant="h6" sx={{ mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                                    <AccountBalance color="action" /> Kütüphane Seçimi
                                </Typography>
                                <FormControl fullWidth required>
                                    <InputLabel id="library-select-label">Eklenecek Kütüphane</InputLabel>
                                    <Select
                                        labelId="library-select-label"
                                        value={selectedLibraryId}
                                        label="Eklenecek Kütüphane"
                                        onChange={(e) => setSelectedLibraryId(e.target.value)}
                                        startAdornment={<InputAdornment position="start"><AccountBalance fontSize="small"/></InputAdornment>}
                                    >
                                        {libraries.map((lib) => (
                                            // Backend'den gelen LibraryDto'da 'id' ve 'categoryName' (veya name) olduğunu varsayıyoruz
                                            <MenuItem key={lib.id} value={lib.id}>
                                                {lib.categoryName}
                                            </MenuItem>
                                        ))}
                                        {libraries.length === 0 && <MenuItem disabled>Yüklü kütüphane yok</MenuItem>}
                                    </Select>
                                </FormControl>
                            </Box>

                            <Divider sx={{ my: 2 }} />

                            {/* Konum Bilgisi */}
                            <Box sx={{ mb: 4 }}>
                                <Typography variant="h6" sx={{ mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                                    <Place color="action" /> Raf Konumu
                                </Typography>
                                <Paper variant="outlined" sx={{ p: 2, backgroundColor: "#f9f9f9" }}>
                                    <Grid container spacing={2}>
                                        <Grid item xs={6}>
                                            <TextField
                                                label="Kat"
                                                name="floor"
                                                value={location.floor}
                                                onChange={handleLocationChange}
                                                fullWidth size="small"
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                label="Oda"
                                                name="room"
                                                value={location.room}
                                                onChange={handleLocationChange}
                                                fullWidth size="small"
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                label="Raf No"
                                                name="shelf"
                                                value={location.shelf}
                                                onChange={handleLocationChange}
                                                fullWidth size="small"
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                label="Bölüm"
                                                name="section"
                                                value={location.section}
                                                onChange={handleLocationChange}
                                                fullWidth size="small"
                                            />
                                        </Grid>
                                    </Grid>
                                </Paper>
                            </Box>

                            {/* Kapak Resmi Yükleme */}
                            <Box>
                                <Typography variant="h6" sx={{ mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                                    <CloudUpload color="action" /> Kapak Resmi
                                </Typography>

                                {!imagePreview ? (
                                    <Button
                                        variant="outlined"
                                        component="label"
                                        fullWidth
                                        sx={{
                                            height: 100,
                                            borderStyle: "dashed",
                                            display: "flex",
                                            flexDirection: "column",
                                            color: "text.secondary"
                                        }}
                                    >
                                        <CloudUpload />
                                        Resim Seç
                                        <input type="file" hidden accept="image/*" onChange={handleImageChange} />
                                    </Button>
                                ) : (
                                    <Box sx={{ position: "relative", width: "100%", height: 200, border: "1px solid #ddd", borderRadius: 2, overflow: "hidden" }}>
                                        <img src={imagePreview} alt="Preview" style={{ width: "100%", height: "100%", objectFit: "contain", backgroundColor: "#f0f0f0" }} />
                                        <IconButton
                                            color="error"
                                            onClick={removeImage}
                                            sx={{ position: "absolute", top: 5, right: 5, backgroundColor: "rgba(255,255,255,0.8)" }}
                                        >
                                            <Delete />
                                        </IconButton>
                                    </Box>
                                )}
                            </Box>
                        </Grid>
                    </Grid>

                    <Divider sx={{ my: 4 }} />

                    {/* --- Kaydet Butonu --- */}
                    <Box sx={{ display: "flex", justifyContent: "flex-end" }}>
                        <Button
                            variant="contained"
                            size="large"
                            onClick={handleSubmit}
                            disabled={loading}
                            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <CloudUpload />}
                            sx={{ px: 5, py: 1.5, borderRadius: 2, fontWeight: "bold", textTransform: "none", fontSize: "1.1rem" }}
                        >
                            {loading ? "İşleniyor..." : "Kitabı Kaydet ve Ata"}
                        </Button>
                    </Box>

                </Paper>
            </Box>

            {/* --- Bildirimler (Snackbar) --- */}
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert onClose={() => setSnackbar({ ...snackbar, open: false })} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
}