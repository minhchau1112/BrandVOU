import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import ListEventComponent from './components/ListEventComponent';
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
              <Route path="/" element={<ListEventComponent />} />
              <Route path="/events" element={<ListEventComponent />} />
              <Route path="/add-events" element={<AddEventComponent brandID={1} />} />
              <Route path="/add-vouchers" element={<AddVoucherComponent brandID={1} />} />
              <Route path="/login" component={Login} />
              <Route path="/register" component={Register} />
            </Routes>
          </div>
        </Router>
      </div>
  );
}

export default App;