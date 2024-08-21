import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BrandNavbar from './components/BrandNavbar';
import AddEventComponent from './components/AddEventComponent';
import AddVoucherComponent from './components/AddVoucherComponent';
import EventList from './components/EventList';

function App() {
  return (
    <div>
      <Router>
        <BrandNavbar />
        <div className="container">
          <Routes>
            <Route path='/' element={<EventList brandID={1} />} />
            <Route path="/add-event" element={<AddEventComponent brandID={1} />} />
            <Route path="/add-voucher" element={<AddVoucherComponent brandID={1} />} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}

export default App;