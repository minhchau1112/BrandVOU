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

function App() {
  return (
    <div>
      <Router>
        <BrandNavbar />
        <div className="container">
          <Routes>
            <Route path='/' element={<EventList brandID={1} />} />
            <Route path='/vouchers' element={<VouchersTable brandID={1} />} />
            <Route path="/add-event" element={<AddEventComponent brandID={1} />} />
            <Route path="/add-voucher" element={<AddVoucherComponent brandID={1} />} />
            <Route path="/vouchers/view-detail/:id" element={<VoucherDetail />} />
            <Route path="/vouchers/edit/:id" element={<VoucherEdit />} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}

export default App;