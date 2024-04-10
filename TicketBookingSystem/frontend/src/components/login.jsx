import React from 'react';
import { useState} from "react";
import {Form, Button, Modal, Alert} from 'react-bootstrap'
import { NavLink } from 'react-router-dom';
import axios from 'axios';

const Login = () => {
    var span_className = "fas fa-user me-1 text-gray fw-normal"
    const [Message, setMessage] = useState("");
    // const [selects, setSelects] = useState();
    const [loginShow, setLoginShow] = useState(false);
    const [signUpshow, setSignupShow] = useState(false);
    const [incorrect, setIncorrect] = useState("d-none")
    const [variant, setVariant] = useState("danger")
    const [SignupFormData, setSignupFormData] = useState({
        name: '',
        email: '',
        password:'',
        role:'Customer',
        wallet: 1000
    });
    const [loginFormData, setLoginFormData] = useState({
        email: '',
        password: ''
    });

    const clearMessage=()=>{
        setMessage("");
        setIncorrect("d-none");
    }

    const handleLogin = () => {
        setLoginShow(true);
    }
    const handleClose = () => {
        setLoginShow(false);
        setSignupShow(false)
        clearMessage();
    };

    const handleBack = () =>{
        setSignupShow(false);
        setLoginShow(true);
        clearMessage();
    }

    const handleRegister = () => {
        setLoginShow(false);
        setSignupShow(true);
    };

    const handleLoginInputChange = (e) => {
        const { name, value } = e.target;
        setLoginFormData({
            ...loginFormData,
            [name]: value,
        });
    };

    const handleSignupInputChange = (e) => {
        const {name, value } = e.target;
        setSignupFormData({
            ...SignupFormData,
            [name]: value,
        });
    }

    const handleLoginSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', loginFormData);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(loginFormData.email, loginFormData.password)
    };

    const handleRegisterSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted: ', SignupFormData);
        if(SignupFormData.password!==SignupFormData.cfmpassword){
            setIncorrect("d-block");
            setVariant("danger");
            setMessage("Passwords don't match")
        }else if(SignupFormData.role===''){
            setIncorrect("d-block");
            setVariant("danger");
            setMessage("Please select your role")
        }
        else{
            send_signUp(SignupFormData.email, SignupFormData.password, SignupFormData.role, SignupFormData.name);
        }
    }


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

                if (status !== 200) {
                    console.log(response);
                    setIncorrect("d-block");
                    setVariant("danger");
                    setMessage("Incorrect email or password")
                } else {
                    console.log(response);
                    localStorage.setItem('token', data.accessToken);
                    // sessionStorage.setItem('token', data.accessToken);
                    window.location.reload(false);
                }
            })
            .catch(function (error) {
                console.error('Error occurred:', error);
                // Handle error here
                setIncorrect("d-block");
                setVariant("danger");
                setMessage("Incorrect email or password")
            });
    }

    const send_signUp = (email, password, role, name) =>{
        let api_endpoint_url = 'http://localhost:8080/createNewUser';
        let formData = new FormData();
        formData.append('name',name);
        formData.append('email',email);
        formData.append('password', password);
        formData.append('role', role);
        formData.append('wallet', 1000);
        console.log(formData);
        axios.post(api_endpoint_url, formData).then(function (response){
            var data = response.data;
            var status = response.request.status;
            console.log(data);

            if (status !== 200) {
                console.log(response);
                console.log("here");
                setIncorrect("d-block");
                setVariant("danger");
            } else {
                console.log(response);
                setIncorrect("d-block");
                setVariant("success");
                setMessage("You have successfully sign up! Return to login page and login");
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
                <NavLink className="nav-link" onClick={handleLogin}><i className={span_className}></i>Log in</NavLink>
            </li>        

            <Modal show = {loginShow} onHide = {handleClose} aria-labelledby="contained-modal-title-vcenter" centered>
                <Modal.Header closeButton>
                    <Modal.Title>Login</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                <Form onSubmit={handleLoginSubmit}>
                    <Alert variant={variant} className={incorrect}>{Message}</Alert>
                    <Form.Group className="mb-3" controlId="email">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control
                            required
                            type="email"
                            name="email"
                            placeholder="name@example.com"
                            autoFocus
                            value={loginFormData.email}
                            onChange={handleLoginInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="password"
                    >
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            required
                            type="password"
                            name='password'
                            placeholder="Password"
                            value={loginFormData.password}
                            onChange={handleLoginInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                        
                        <Button className="ml-6 w-100" type="submit">
                            Login
                        </Button>
                        <hr></hr>
                        <p>Don't have an account?</p>
                        <Button variant = "outline-secondary w-100" onClick={handleRegister}>
                            Sign Up
                        </Button>
                </Form>

                </Modal.Body>
            </Modal>
            <Modal show={signUpshow} onHide = {handleClose} aria-labelledby="contained-modal-title-vcenter" centered>
                <Modal.Header closeButton>
                    <Modal.Title>Registration</Modal.Title>
                </Modal.Header>
                
                <Modal.Body>
                <Form onSubmit={handleRegisterSubmit}>
                    <Alert variant={variant} className={incorrect}>{Message}</Alert>
                    {/* <Form.Select required name="role" defaultValue="" onChange={handleSignupInputChange}>
                        <option disabled value="">Select your role</option>
                        <option>Customer</option>
                        <option>Event_Manager</option>
                    </Form.Select> */}
                    <Form.Group className="mb-3" controlId="name">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            required
                            type="name"
                            name="name"
                            placeholder="Please enter your name"
                            autoFocus
                            value={SignupFormData.name}
                            onChange={handleSignupInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="email">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control
                            required
                            type="email"
                            name="email"
                            placeholder="name@example.com"
                            autoFocus
                            value={SignupFormData.email}
                            onChange={handleSignupInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="password"
                    >
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            required
                            type="password"
                            name='password'
                            placeholder="Password"
                            value={SignupFormData.password}
                            onChange={handleSignupInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="cfmpassword"
                    >
                        <Form.Label>Re-enter Password</Form.Label>
                        <Form.Control
                            required
                            type="password"
                            name='cfmpassword'
                            placeholder="Password"
                            value={SignupFormData.cfmpassword}
                            onChange={handleSignupInputChange}
                            autoComplete="off"
                        />
                    </Form.Group>
                        <Button variant = "secondary" onClick={handleBack}>
                            Back
                        </Button>
                        
                        <Button variant = "outline-secondary" type = "submit">
                            Sign Up
                        </Button>
                    
                </Form>

                </Modal.Body>
            </Modal>
        </>

    )
}
export default Login