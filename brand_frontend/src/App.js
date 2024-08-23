import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BrandNavbar from './components/BrandNavbar';
import AddEventComponent from './components/AddEventComponent';
import AddVoucherComponent from './components/AddVoucherComponent';
import Login from './components/LoginFormComponent';
import Register from './components/RegisterFormComponent';

function App() {
  return (
      <div>
        <Router>
          <div className="container">
            <Routes>
              <Route path="/add-events" element={<AddEventComponent brandID={1} />} />
              <Route path="/add-vouchers" element={<AddVoucherComponent brandID={1} />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
            </Routes>
          </div>
        </Router>
      </div>
  );
}

export default App;