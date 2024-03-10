import React from 'react';
import { useState } from "react";
import {Form, Button, Modal, Alert} from 'react-bootstrap'
import axios from 'axios';
import { NavLink } from 'react-router-dom';

function Login(){
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    var span_className = "fas fa-user me-1 text-gray fw-normal"
    const [incorrect, setIncorrect] = useState("d-none")
    const [variant, setVariant] = useState("danger")

    const [formData, setFormData] = useState({
        email: '',
        password: ''
    })

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', formData);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(formData.email, formData.password)
    };
    
    const send_onsubmit = (email, password) => {
        let api_endpoint_url = 'http://localhost:8080/login';
        let formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);
    
        axios.post(api_endpoint_url, formData)
            .then(function (response) {
                var data = response.data;
                var status = response.request.status;
                console.log(data);

                if (status != 200) {
                    console.log(response);
                    console.log("here");
                    setIncorrect("d-block");
                    setVariant("danger");
                } else {
                    console.log(response);
                    sessionStorage.setItem('token', data.accessToken);
                    window.location.reload(false);
                }
            })
            .catch(function (error) {
                console.error('Error occurred:', error);
                // Handle error here
            });
    }

    return (
        <>
            <li className="nav-item">
                <NavLink className="nav-link" onClick={handleShow}><i className={span_className}></i>Log in</NavLink>
            </li>
            
            <Modal show = {show} onHide = {handleClose} aria-labelledby="contained-modal-title-vcenter" centered>
                <Modal.Header>
                    <Modal.Title>Login</Modal.Title>
                </Modal.Header>
                
                <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Alert variant={variant} className={incorrect}>Incorrect email or password</Alert>
                    <Form.Group className="mb-3" controlId="email">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control
                            type="email"
                            name="email"
                            placeholder="name@example.com"
                            autoFocus
                            value={formData.email}
                            onChange={handleInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="password"
                    >
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            name='password'
                            placeholder="Password"
                            value={formData.password}
                            onChange={handleInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>

                    <Button variant = "secondary" onClick = {handleClose}>
                        Close
                    </Button>

                    <Button className="ml-2" variant = "primary" type="submit">
                        Login
                    </Button>
                </Form>

                </Modal.Body>
            </Modal>
        </>
    )
}
export default Login