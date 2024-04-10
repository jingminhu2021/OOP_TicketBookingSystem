import { useState, useEffect } from "react";
import Login from "./login";
import { Link } from "react-router-dom";
import axios from "axios";
import { Nav, Navbar, NavDropdown } from "react-bootstrap";


function Navibar(){
  const token = localStorage.getItem('token');
  const config = {
      headers: { Authorization: `Bearer ${token}` }
  };
  const [userData, setUserData] = useState(null);

  useEffect(() => {
      const getUserData = async (token) => {
          let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
          const bodyParameters = {
              key: "value"
          };
          try {
              const response = await axios.post(api_endpoint_url, bodyParameters, config);
              setUserData(response.data);
              // console.log(response.data.role)
          } catch (error) {
              console.error('Error occurred:', error);
          }
      };
      if (token) {
          getUserData(token);
      }
  }, []); 
  function checkUser(){
    // if (sessionStorage.getItem('token') != null){
    if (token != null){
      // console.log("current token:"+`${token}`);
      return(
          <Nav.Item className="nav-item">
            <Nav.Link className="nav-link" href="/logout">Logout</Nav.Link>
          </Nav.Item>          

      )
    }else{
      return(
        // <li className="nav-item"><a className="nav-link" href="#!"> <i className="fas fa-user me-1 text-gray fw-normal"></i>Login</a></li>
        <Login />
      )
    }
  }
  function renderBookingHistory(){
    if (userData && userData.role === 'Customer'){
      return(
        <Nav.Item className="nav-item">
          <Nav.Link className="nav-link" href="/viewBookingHistory">Booking History</Nav.Link>
        </Nav.Item>
      )
    }
  }
  function CheckRole(){
  if (userData && userData.role === 'Event_Manager'){
    // console.log(userData);
    return(
      <Nav.Item className="nav-item">
        <Nav.Link className="nav-link" href="/manageEvents">Manage Events</Nav.Link>
      </Nav.Item>
    )
  }
     // Empty dependency array ensures this effect runs only once
  }

  function renderSalesStatistics(){
    if (userData && userData.role === 'Event_Manager'){
      // console.log(userData);
      return(
        <NavDropdown title="Event Statistics" className="nav-item dropdown">
          <NavDropdown.Item className="dropdown-item border-0 transition-link" href="/viewSalesStatistics">Single Event</NavDropdown.Item>
          <NavDropdown.Item className="dropdown-item border-0 transition-link" href="/viewAllSalesStatistics">All Events</NavDropdown.Item>
      </NavDropdown>
      )
    }
       // Empty dependency array ensures this effect runs only once
    }

    return(
        <header className="header bg-white">
        <div className="container px-lg-3">
          <Nav className="navbar navbar-expand-lg py-3 px-lg-0"><a className="navbar-brand" href="/"><span className="fw-bold text-uppercase text-dark">TBS</span></a>
            <button className="navbar-toggler navbar-toggler-end" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span className="navbar-toggler-icon"></span></button>
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
              <Navbar>
                <Nav.Item className="me-auto">
                  <Nav.Link className="nav-link" href="/">Home</Nav.Link>
                </Nav.Item>
                {CheckRole()}
              </Navbar>
              <Navbar className="ms-auto">               
                {/* <li className="nav-item"><a className="nav-link" href="cart.html"> <i className="fas fa-dolly-flatbed me-1 text-gray"></i>Cart<small className="text-gray fw-normal">(2)</small></a></li>
                <li className="nav-item"><a className="nav-link" href="#!"> <i className="far fa-heart me-1"></i><small className="text-gray fw-normal"> (0)</small></a></li> */}
                {renderSalesStatistics()}
                {renderBookingHistory()}
                {checkUser()}
              </Navbar>
            </div>
          </Nav>
        </div>
      </header>

    )
}

export default Navibar;