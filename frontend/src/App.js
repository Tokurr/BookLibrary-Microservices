import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminLogin from "./pages/AdminLogin";

import ProtectedRoute from "./ProtectedRoute";
import Books from "./pages/Books";
import AddBook from "./pages/AddBook";
import Libraries from "./pages/Libraries";

function App() {
    return (
        <Router>
            <Routes>
                {/* Login sayfası */}
                <Route path="/" element={<AdminLogin />} />

                {/* Admin layout */}
                <Route
                    path="/admin"
                    element={
                        <ProtectedRoute>
                            <Navigate to="/admin/books" replace />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/books"
                    element={
                        <ProtectedRoute>
                            <Books />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/add-book"
                    element={
                        <ProtectedRoute>
                            <AddBook />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/libraries"
                    element={
                        <ProtectedRoute>
                            <Libraries />
                        </ProtectedRoute>
                    }
                />

            </Routes>
        </Router>
    );
}

export default App;
