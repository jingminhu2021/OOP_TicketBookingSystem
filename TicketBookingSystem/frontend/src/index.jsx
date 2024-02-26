import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import Home from './pages/home.jsx';

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
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/home" element={<Home />} />
      </Routes>
    </BrowserRouter>
  )
}


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <App />
);
