import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import BrandNavbar from './components/BrandNavbar';
import AddEventComponent from './components/AddEventComponent';
import AddVoucherComponent from './components/AddVoucherComponent';
import EventList from './components/EventList';
import VouchersTable from './components/VouchersTable';
import VoucherDetail from './components/VoucherDetail';
import VoucherEdit from './components/VoucherEdit';
import EventDetail from './components/EventDetail';
import EventEdit from './components/EventEdit';
import Login from './components/LoginComponent';
import Register from './components/RegisterComponent';
import AuthProvider from  './AuthProvider';
import PrivateRoute from "./PrivateRoute";
import { useAuth } from "./AuthProvider";

function App() {
    return (
        <Router>
            <AuthProvider>
                <BrandNavbar />

                <div className="container">
                    <Routes>
                        <Route element={<PrivateRoute />}>
                            <Route path='/' element={<EventList brandID={1} />} />
                            <Route path='/vouchers' element={<VouchersTable brandID={1} />} />
                            <Route path="/add-event" element={<AddEventComponent brandID={1} />} />
                            <Route path="/add-voucher" element={<AddVoucherComponent brandID={1} />} />
                            <Route path="/vouchers/view-detail/:id" element={<VoucherDetail />} />
                            <Route path="/vouchers/edit/:id" element={<VoucherEdit />} />
                            <Route path="/events/view-detail/:id" element={<EventDetail />} />
                            <Route path="/events/edit/:id" element={<EventEdit />} />
                        </Route>

                        <Route path="/login" element={<Login/>} />
                        <Route path="/register" element={<Register/>} />
                    </Routes>
                </div>
            </AuthProvider>
        </Router>
    );
}

export default App;