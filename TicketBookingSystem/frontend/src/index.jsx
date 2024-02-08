import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import Home from './pages/home.jsx';

export default function App() {
  return (
    <div>
      <Home />
    </div>
  )
}


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <App />
);
