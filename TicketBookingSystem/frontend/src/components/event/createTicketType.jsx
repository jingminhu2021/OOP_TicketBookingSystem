import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Form, InputGroup, Alert } from 'react-bootstrap';

function CreateTicketType(id){
    // const id = props.event_id;
    console.log("event id", id)
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const handleClose2 = () => {
        setShow2(false);
        setFormData([{ category: '', price: '', number_of_ticket: '', cancellation_fee: ''}]);
        setResults([]);
        window.location.reload();
    }
    const handleShow2 = () => setShow2(true);

    const [show3, setShow3] = useState(false);
    const handleClose3 = () => setShow3(false);
    
    const handleShow3 = () => setShow3(true);

    const [showCategoryAlert, setShowCategoryAlert] = useState(false);
    const [showPriceAlert, setShowPriceAlert] = useState(false);
    const [showNumberOfTicketAlert, setShowNumberOfTicketAlert] = useState(false);
    const [showCancellationFeeAlert, setShowCancellationFeeAlert] = useState(false);

    const [formData, setFormData] = useState([{ category: '', price: '', number_of_ticket: '', cancellation_fee: ''}]);
    const [results, setResults] = useState([]);
    const [duplicate, setDuplicate] = useState([]);

    const handleAddClick = () => {
        setFormData([...formData, { category: '', price: '', number_of_ticket: '', cancellation_fee: ''}]);
    };

    const handleDeleteClick = index => {
        const list = [...formData];
        list.splice(index, 1);
        setFormData(list);
    };
    

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        if (token) {
            getUserData(token);
        }
        
    }, []); // Empty dependency array ensures this effect runs only once

    const handleInputChange = (e, index) => {
        const { name, value } = e.target;
        const list = [...formData];
        if (!value) {
            switch (name) {
                case 'category':
                    setShowCategoryAlert(true);
                    break;
                case 'price':
                    setShowPriceAlert(true);
                    break;
                case 'number_of_ticket':
                    setShowNumberOfTicketAlert(true);
                    break;
                case 'cancellation_fee':
                    setShowCancellationFeeAlert(true);
                    break;
                default:
                    break;
            }
        }
        // Check if value is within range for 'cancellation_fee' field
        if (name === 'cancellation_fee') {
            if (value < 0) {
                list[index][name] = 0;
            } else if (value > 100) {
                list[index][name] = 100;
            }  else {
                list[index][name] = value;
            }
            
        } else {
            list[index][name] = value;
        }
        setFormData(list);
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Check for duplicate categories
        const categories = formData.map(item => item.category);
        const hasDuplicates = categories.some((category, index) => categories.indexOf(category) !== index);

        if (hasDuplicates) {
            setDuplicate(categories.filter((category, index) => categories.indexOf(category) !== index));
            handleShow3();
            return;
        }
        
        if (formData.length === 0) {
            console.log('No data to submit');
            return;
        }
        console.log('Form data submitted:', formData);
        // Add your authentication logic here, e.g., send data to an API
        for (let key in formData) {
            send_onsubmit(id, formData[key].category, formData[key].price, formData[key].cancellation_fee,  formData[key].number_of_ticket);
        }
        handleShow2();
        handleClose();        
    };

    const send_onsubmit = (event_id, category, price, cancellation_fee, number_of_ticket) => {
        let api_endpoint_url = 'http://localhost:8080/ticketType/createTicketType';
        var bodyParameters = {
            eventId: event_id,
            eventCat: category,
            eventPrice: price,
            cancellationFeePercentage: cancellation_fee,
            numberOfTix: number_of_ticket
        };

        axios.post(api_endpoint_url, bodyParameters, config)
        .then((response) => {
            console.log(response);
            setResults(prevResults => [...prevResults, response]);
        })
        .catch((error) => {
            console.error('Error occurred:', error);
        });
        
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="primary" className='w-100' onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Create Ticket Type
            </Button>
        )}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Create Ticket Type</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    {formData.map((item, index) => (

                        <Form.Group className="mb-3" controlId={`ticketType${index}`} key={index}>
                            <h5>Add new Ticket {index+1}:</h5>
                            
                            <Form.Label>Ticket Type:</Form.Label>
<<<<<<< HEAD
                            <Form.Control type="text" placeholder="Enter Ticket Type name" name="category" value={item.category} onChange={e => handleInputChange(e, index)} required/>
                            {showCategoryAlert && 
                                <Alert variant="danger" onClose={() => setShowCategoryAlert(false)} dismissible>
                                    <p>
                                        Ticket Type name is required.
=======
                            <Form.Control type="text" placeholder="Enter ticket type" name="category" value={item.category} onChange={e => handleInputChange(e, index)} required/>
                            {showCategoryAlert && 
                                <Alert variant="danger" onClose={() => setShowCategoryAlert(false)} dismissible>
                                    <p>
                                        Ticket type is required.
>>>>>>> 718e98fc5aee01eb5d404ff8dc334057f73c6f57
                                    </p>
                                </Alert>
                            }

                            
                            <Form.Label>Event Price:</Form.Label>
                            <InputGroup>
                                <InputGroup.Text id="basic-addon1">SGD$</InputGroup.Text>
                                <Form.Control type="number" placeholder="Enter price" name="price" value={item.price} onChange={e => handleInputChange(e, index)} required/>

                            </InputGroup>
                            {showPriceAlert && 
                                <Alert variant="danger" onClose={() => setShowPriceAlert(false)} dismissible>
                                    <p>
                                        Price is required.
                                    </p>
                                </Alert>
                            }

                            
                            <Form.Label>Number of ticket:</Form.Label>
                            <Form.Control type="number" placeholder="Enter number of ticket" name="number_of_ticket" value={item.number_of_ticket} onChange={e => handleInputChange(e, index)} required/>
                            {showNumberOfTicketAlert && 
                                <Alert variant="danger" onClose={() => setShowNumberOfTicketAlert(false)} dismissible>
                                    <p>
                                        Number of ticket is required.
                                    </p>
                                </Alert>
                            }
                            

                            <Form.Label> Cancellation fee (Percentage)</Form.Label>

                            <InputGroup>
                                <Form.Control type="number" min="0" max="100" placeholder="Enter cancellation fee" name="cancellation_fee" value={item.cancellation_fee} onChange={e => handleInputChange(e, index)} required/>
                                <InputGroup.Text id="basic-addon1">%</InputGroup.Text>
                            </InputGroup>
                            {showCancellationFeeAlert && 
                                <Alert variant="danger" onClose={() => setShowCancellationFeeAlert(false)} dismissible>
                                    <p>
                                        Cancellation fee is required.
                                    </p>
                                </Alert>
                            }

                            <br></br>
                            <Button variant="danger" onClick={() => handleDeleteClick(index)}>Delete</Button>
                            <hr/>
                        </Form.Group>
                    ))}
                    <div className="d-flex justify-content-between">
                        <Button onClick={handleAddClick}>Add more ticket type</Button>
                        <Button variant="primary" type="submit">
                            Create
                        </Button>
                    </div>
  
                </Form>
            </Modal.Body>
        </Modal>
        
        <Modal show={show2} onHide={handleClose2}>
            <Modal.Header closeButton>
                <Modal.Title>Creation result</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {results.map((results, index) => (
                    
                    <div key={index}>
                        <p>Category: {JSON.parse(results.config.data).eventCat}</p>
                        <p>Message: {results.data.message }</p>
                        {/* <p>Success? {results.status ? "Yes":"No"}</p> */}
                        <hr/>
                    </div>
                ))}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose2}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>

        <Modal show={show3} onHide={handleClose3}>
            <Modal.Header closeButton>
                <Modal.Title>Duplicate categories</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Duplicate categories are not allowed.</p>
                <p>Duplicate categories are:</p>
                {duplicate.map((item, index) => (
                    <p key={index}>{item}</p>
                ))}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose3}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
        
        </>
    )
}

export default CreateTicketType;