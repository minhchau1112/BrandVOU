import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
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

function App() {
  return (
    <div>
      <Router>
        <BrandNavbar />
        <div className="container">
          <Routes>
            <Route path='/' element={<EventList/>} />
            <Route path='/vouchers' element={<VouchersTable/>} />
            <Route path="/add-event" element={<AddEventComponent/>} />
            <Route path="/add-voucher" element={<AddVoucherComponent/>} />
            <Route path="/vouchers/view-detail/:id" element={<VoucherDetail/>} />
            <Route path="/vouchers/edit/:id" element={<VoucherEdit/>} />
            <Route path="/events/view-detail/:id" element={<EventDetail/>} />
            <Route path="/events/edit/:id" element={<EventEdit/>} />
            <Route path="/login" element={<Login/>} />
            <Route path="/register" element={<Register/>} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}

export default App;