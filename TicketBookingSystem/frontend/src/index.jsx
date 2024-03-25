import React, { useState, useEffect} from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import axios from 'axios';
import './index.css';
import Home from './pages/home.jsx';
import Logout from './components/logout.jsx';
import VerifyTicket from './pages/verifyTicket.jsx';


import "./css/style.blue.css";
import "./css/style.blue.min.css";
import "./css/style.blue.min.css.map";
import "./css/style.default.css";
import "./css/style.default.min.css";
import "./css/style.default.min.css.map";
import "./css/style.gold.css";
import "./css/style.gold.min.css";
import "./css/style.gold.min.css.map";
import "./css/style.green.css";
import "./css/style.green.min.css";
import "./css/style.green.min.css.map";
import "./css/style.pink.css";
import "./css/style.pink.min.css";
import "./css/style.pink.min.css.map";
import "./css/style.red.css";
import "./css/style.red.min.css";
import "./css/style.red.min.css.map";
import "./css/style.sea.css";
import "./css/style.sea.min.css";
import "./css/style.sea.min.css.map";
import "./css/style.violet.css";
import "./css/style.violet.min.css";
import "./css/style.violet.min.css.map";

export default function App() {
  // const token = sessionStorage.getItem('token');
  const token = localStorage.getItem('token');
  const config = {
      headers: { Authorization: `Bearer ${token}` }
  };
  const [userData, setUserData] = useState(null);

  useEffect(() => {
      console.log('Bearer ' + token);
      const getUserData = async (token) => {
          let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
          const bodyParameters = {
              key: "value"
          };
          try {
              const response = await axios.post(api_endpoint_url, bodyParameters, config);
              setUserData(response.data);
              console.log(response.data.role)
          } catch (error) {
              console.error('Error occurred:', error);
          }
      };
      if (token) {
          getUserData(token);
      }
  }, []); // Empty dependency array ensures this effect runs only once

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/logout" element={<Logout />} />

        // Check if the user is a Ticketing Officer
        {userData && userData.role === 'Ticketing_Officer' ? (
          <Route path="/verifyTicket" element={<VerifyTicket />} />
        ) : (
          <Route path="/verifyTicket" element={<h1>Not Authorized</h1>} />
        )}

        <Route path="*" element={<h1>Not Found</h1>} />
      </Routes>
    </BrowserRouter>
  )
}


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <App />
);
