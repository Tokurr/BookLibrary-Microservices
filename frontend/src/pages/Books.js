import { useEffect, useState, useCallback } from "react";
import api from "../api/api";
import Sidebar from "../components/Sidebar";
import Grid from "@mui/material/Grid";   // 🟢 BURASI ÖNEMLİ

import {
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Typography,
    Pagination,
    Dialog,
    DialogTitle,
    DialogContent,
    TextField,
    DialogActions
} from "@mui/material";


export default function Books() {
    const [books, setBooks] = useState([]);
    const [page, setPage] = useState(1);
    const [size] = useState(8);
    const [totalPages, setTotalPages] = useState(1);

    // Güncelleme State'leri
    const [openEdit, setOpenEdit] = useState(false);
    const [editBook, setEditBook] = useState({
        id: "",
        title: "",
        bookYear: "",
        author: "",
        pressName: "",
        isbn: "",
        description: ""
    });

    const loadBooks = async () => {
        try {
            const res = await api.get(`/v1/book/list?page=${page - 1}&size=${size}`);
            if (Array.isArray(res.data)) {
                setBooks(res.data);
            } else {
                setBooks(res.data.content || []);
                setTotalPages(res.data.totalPages || 1);
            }
        } catch (err) {
            console.error(err);
        }
    };

    const deleteBook = async (id) => {
        if (!window.confirm("Bu kitabı silmek istiyor musun?")) return;
        try {
            // Token api.js sayesinde otomatik eklenecek
            await api.delete(`/v1/book/delete/${id}`);

            setBooks((prev) => prev.filter((b) => b.id !== id));
            loadBooks();
        } catch (err) {
            console.error(err);
            if (err.response && err.response.status === 403) {
                alert("❌ Bu işlemi yapmaya yetkiniz yok!");
            } else {
                alert("❌ Kitap silinirken hata oluştu.");
            }
        }
    };

    // --- GÜNCELLEME ---
    const handleEditClick = (book) => {
        setEditBook({
            id: book.id,
            title: book.title || "",
            bookYear: book.bookYear || "",
            author: book.author || "",
            pressName: book.pressName || "",
            isbn: book.isbn || "",
            description: book.description || ""
        });
        setOpenEdit(true);
    };

    const handleEditChange = (e) => {
        setEditBook({ ...editBook, [e.target.name]: e.target.value });
    };

    const handleUpdateSave = async () => {
        try {
            const payload = {
                title: editBook.title,
                bookYear: parseInt(editBook.bookYear) || 0, // Integer kontrolü
                author: editBook.author,
                pressName: editBook.pressName,
                isbn: editBook.isbn,
                description: editBook.description
            };

            // api.put çağrısında token otomatik gönderilecek (api.js sayesinde)
            await api.put(`/v1/book/update/${editBook.id}`, payload);

            alert("✅ Kitap başarıyla güncellendi.");
            setOpenEdit(false);
            loadBooks();

        } catch (err) {
            console.error("Güncelleme hatası:", err);

            // Token veya yetki hatalarını yakala
            if (err.response) {
                if (err.response.status === 401) {
                    alert("❌ Oturum süreniz dolmuş, lütfen tekrar giriş yapın.");
                    // İsterseniz burada login sayfasına yönlendirebilirsiniz
                    // window.location.href = "/login";
                } else if (err.response.status === 403) {
                    alert("❌ Bu işlem için yetkiniz bulunmuyor.");
                } else {
                    alert(`❌ Bir hata oluştu: ${err.response.status}`);
                }
            }
        }
    };

    useEffect(() => {
        loadBooks();
    }, [page]);

    return (
        <Box sx={{ display: "flex" }}>
            <Sidebar />

            <Box sx={{ padding: 4, flex: 1 }}>
                <Typography variant="h5" sx={{ mb: 3, fontWeight: 600 }}>
                    📚 Kitap Yönetimi
                </Typography>

                <Grid container spacing={2}>
                    {books.map((book) => (
                        <Grid item xs={12} sm={6} md={3} key={book.id}>
                            <Card
                                sx={{
                                    padding: "10px",
                                    borderRadius: "10px",
                                    minHeight: "250px",
                                    display: "flex",
                                    flexDirection: "column",
                                    justifyContent: "space-between"
                                }}
                            >
                                <Box>
                                    {book.coverImage && (
                                        <CardMedia
                                            component="img"
                                            height="120"
                                            image={`data:image/jpeg;base64,${book.coverImage}`}
                                            sx={{ objectFit: "contain" }}
                                        />
                                    )}
                                    <CardContent>
                                        <Typography variant="subtitle1" noWrap><b>{book.title}</b></Typography>
                                        <Typography variant="body2">✍️ {book.author}</Typography>
                                        <Typography variant="caption" display="block">ISBN: {book.isbn}</Typography>
                                    </CardContent>
                                </Box>

                                <Box sx={{ p: 2, display: "flex", justifyContent: "space-between" }}>
                                    <Button size="small" color="info" variant="outlined" onClick={() => handleEditClick(book)}>
                                        Düzenle
                                    </Button>
                                    <Button size="small" color="error" variant="contained" onClick={() => deleteBook(book.id)}>
                                        Sil
                                    </Button>
                                </Box>
                            </Card>
                        </Grid>
                    ))}
                </Grid>


                <Box sx={{ mt: 4, display: "flex", justifyContent: "center" }}>
                    <Pagination
                        count={totalPages}
                        page={page}
                        onChange={(e, value) => setPage(value)}
                    />
                </Box>

                {/* --- UPDATE MODAL --- */}
                <Dialog open={openEdit} onClose={() => setOpenEdit(false)} maxWidth="sm" fullWidth>
                    <DialogTitle>Kitap Bilgilerini Güncelle</DialogTitle>
                    <DialogContent>
                        <TextField margin="dense" name="title" label="Kitap Adı" fullWidth variant="outlined" value={editBook.title} onChange={handleEditChange} />
                        <TextField margin="dense" name="author" label="Yazar" fullWidth variant="outlined" value={editBook.author} onChange={handleEditChange} />
                        <Grid container spacing={2}>
                            <Grid size={{ xs: 6 }}>
                                <TextField margin="dense" name="bookYear" label="Basım Yılı" type="number" fullWidth variant="outlined" value={editBook.bookYear} onChange={handleEditChange} />
                            </Grid>
                            <Grid size={{ xs: 6 }}>
                                <TextField margin="dense" name="isbn" label="ISBN" fullWidth variant="outlined" value={editBook.isbn} onChange={handleEditChange} />
                            </Grid>
                        </Grid>
                        <TextField margin="dense" name="pressName" label="Yayınevi" fullWidth variant="outlined" value={editBook.pressName} onChange={handleEditChange} />
                        <TextField margin="dense" name="description" label="Açıklama" fullWidth multiline rows={3} variant="outlined" value={editBook.description} onChange={handleEditChange} />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setOpenEdit(false)} color="secondary">İptal</Button>
                        <Button onClick={handleUpdateSave} variant="contained" color="primary">Kaydet</Button>
                    </DialogActions>
                </Dialog>
            </Box>
        </Box>
    );
}