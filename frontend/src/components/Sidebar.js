import { Link } from "react-router-dom";
import { Box, Button } from "@mui/material";

export default function Sidebar() {
    return (
        <Box sx={{ width: "220px", background: "#e5e5e5", padding: 3, height: "100vh" }}>
            <Link to="/admin/books">
                <Button fullWidth>📚 Kitaplar</Button>
            </Link>

            <Link to="/admin/add-book">
                <Button fullWidth>➕ Kitap Ekle</Button>
            </Link>

            <Link to="/admin/libraries">
                <Button fullWidth>🏛 Kütüphaneler</Button>
            </Link>

        </Box>
    );
}
