import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Form, Modal, Button} from 'react-bootstrap';

function BookTicket(props){
    var eventData = props.eventData;
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);
    const [results, setResults] = useState({
        message: 'Payment is processing...'
    });
    const handleShow = () => setShow(true);
    const handleClose = () => {setShow(false);
        setFormData({
            ...formData,
            ticketAndQuantity: Object.fromEntries(
                eventData.ticketTypes.map((ticket) => [ticket.eventCat, 0])
            )
        });
    }
    const handleClose2 = () => {
        if(results.status===true){
            window.location.reload();
        }
        setShow2(false);
        setResults({message: 'Payment is processing...'});
    }
    

    const token = localStorage.getItem('token');
    const [userData, setUserData] = useState(null);
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };

    useEffect(() => {
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            const bodyParameters = {
                key: "value"
            };
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
    }, []); 

    const [formData, setFormData] = useState({
        eventId: eventData.event.id,
        ticketAndQuantity: Object.fromEntries(
            eventData.ticketTypes.map((ticket) => [ticket.eventCat, 0])
        ),        paymentMethod:``
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) =>{
        e.preventDefault();
        setShow2(true);

        if(token===null){
            setResults({message: 'Please login before making a purchase.'});
        }else if (formData.paymentMethod===''){
            setResults({message: 'Please select a payment method.'});
        }else if (!validateForm()) {
            setResults({message: 'Please purchase at least 1 ticket and ensure the total quantity does not exceed 5.'});
        }
        else{
            if(userData && validateForm()){
                if(userData.role === 'Customer'){
                    send_onsubmit(userData.user.id,formData.eventId,formData.ticketAndQuantity, formData.paymentMethod);
                }else if (userData.role === 'Ticketing_Officer'){
                    send_onsitesubmit(userData.user.id, formData.customerName, formData.customerEmail, formData.eventId,formData.ticketAndQuantity, formData.paymentMethod);
                }
            }
        }
    };


    const send_onsubmit = (userId, eventId,ticketAndQuantity,paymentMethod)=>{
        let api_endpoint_url = 'http://localhost:8080/transaction/bookTicket'
        let eventCats =[];
        let ticketQuantity =[];
        // console.log(formData)
        // console.log(ticketAndQuantity)
        Object.keys(ticketAndQuantity).forEach((eventCat) => {
            eventCats.push(eventCat);
            ticketQuantity.push(ticketAndQuantity[eventCat]);
        });
        // console.log(eventCats);
        let data = {
            userId: userId,
            eventId: eventId,
            eventCats: eventCats,
            eachCatTickets: ticketQuantity,
            paymentMode: paymentMethod
        }
        // console.log(data);

        axios.post(api_endpoint_url, data, config)
            .then(function (response){
                var data = response.data;
                setResults(data);
                // console.log(data);

                // Redirect to Stripe payment if paymentMode is not wallet
                if (paymentMethod !== 'wallet' && data.includes('http')){
                    setShow2(false);
                    window.location.href = data;
                }

                if(data.status===true){
                    handleClose();
                }
            }).catch(function (error){
                console.error(`Error occurred:`, error);
                setResults({message: 'ticket is out of stock.'});
            });
    }

    const send_onsitesubmit = (ticketOfficerId, customerName, customerEmail, eventId,ticketAndQuantity, paymentMethod)=>{
        let api_endpoint_url = 'http://localhost:8080/transaction/onSiteBookTicket'
        let eventCats =[];
        let ticketQuantity =[];
        Object.keys(ticketAndQuantity).forEach((eventCat) => {
            eventCats.push(eventCat);
            ticketQuantity.push(ticketAndQuantity[eventCat]);
        });
        let data = {
            userId: ticketOfficerId,
            name: customerName,
            email: customerEmail,
            eventId: eventId,
            eventCats: eventCats,
            eachCatTickets: ticketQuantity,
            paymentMode: paymentMethod
        }
        console.log(data);

        axios.post(api_endpoint_url, data, config)
            .then(function (response){
                var data = response.data;
                setResults(data);

                // Redirect to Stripe payment if paymentMode is not wallet
                if (data.status === undefined){
                    if (paymentMethod !== 'wallet' && data.includes('http')){
                        setShow2(false);
                        window.location.href = data;
                    }
                }
                

                if(data.status===true){
                    handleClose();
                }
            }).catch(function (error){
                console.error(`Error occurred:`, error);
                setResults({message: 'Something went wrong.'});
            });
    }

    const handleQuantityChange = (e, eventCat) => {
        const newValue = parseInt(e.target.value);
        const updatedFormData = { ...formData };
    
        if (!isNaN(newValue) && newValue >= 0) {
            updatedFormData.ticketAndQuantity[eventCat] = newValue;
        } else {
            updatedFormData.ticketAndQuantity[eventCat] = 0;
        }
    
        setFormData(updatedFormData);
    };

    const showTicketOptions = () => {
        return eventData.ticketTypes.map((ticket) => (
            <div key={ticket.eventCat} className="mb-3 p-4 bg-light rounded">
                <div className="d-flex justify-content-between align-items-center">
                    <Form.Label className="fs-5 fw-bold">{ticket.eventCat} (${ticket.eventPrice})  available: {ticket.numberOfTix}</Form.Label>
                    {formData.ticketAndQuantity.hasOwnProperty(ticket.eventCat) && (
                        <Form.Group controlId={`ticketQuantity-${ticket.eventCat}`} className="mb-0">
                            <div className="input-group">
                                <button
                                    className="btn btn-dark btn-sm"
                                    type="button"
                                    onClick={() => handleQuantityChange({ target: { value: formData.ticketAndQuantity[ticket.eventCat] - 1 }}, ticket.eventCat)}
                                > - </button>
                                <input
                                    type="text"
                                    className="form-control text-center"
                                    name={`ticketQuantity-${ticket.eventCat}`}
                                    value={formData.ticketAndQuantity[ticket.eventCat] || 0}
                                    onChange={(e) => handleQuantityChange(e, ticket.eventCat)}
                                    style={{ width: '50px', margin: '0 5px' }}
                                />
                                <button
                                    className="btn btn-dark btn-sm"
                                    type="button"
                                    onClick={() => handleQuantityChange({ target: { value: formData.ticketAndQuantity[ticket.eventCat] + 1 }}, ticket.eventCat)}
                                > + </button>
                            </div>
                        </Form.Group>
                    )}
                </div>
            </div>
        ));
    };

    const validateForm = () => {
        const totalQuantity = Object.values(formData.ticketAndQuantity)
            .reduce((total, quantity) => total + parseInt(quantity || 0), 0);
    
        if (Object.keys(formData.ticketAndQuantity).length === 0 || totalQuantity === 0 || totalQuantity > 5) {
            return false;
        }
        return true;
    };

    function checkTicketingOfficer(){
        if (userData && userData.role === 'Ticketing_Officer'){
            return (
                
                <Form.Group className="mb-3" controlId="customerEmail">
                <Form.Label>Customer Name</Form.Label>
                <Form.Control
                    required
                    type="text"
                    name="customerName"
                    placeholder="Please enter Customer Name"
                    autoFocus
                    value={formData.customerName}
                    onChange={handleInputChange}
                    autoComplete="off"
                />

                <Form.Label>Customer Email</Form.Label>
                <Form.Control
                    required
                    type="email"
                    name="customerEmail"
                    placeholder="Please enter Customer Email"
                    autoFocus
                    value={formData.customerEmail}
                    onChange={handleInputChange}
                    autoComplete="off"
                />
                </Form.Group>
            )
        }
    }

    return(
        <>
            {/* <div class="col-sm-3 pl-sm-0"><a class="btn btn-dark btn-sm btn-block h-100 d-flex align-items-center justify-content-center px-0" onClick={handleShow} onHide={handleClose}>Book a ticket</a></div> */}
            {userData && (userData.role === 'Customer') ? (
                <Button className='w-100' style={{height: '60px'}} variant='primary' onClick={handleShow}>
                    <i className="fas fa-plus me-1 text-gray fw-normal"></i>Book a ticket
                </Button>
            ) : userData && (userData.role === 'Ticketing_Officer') ? (
                <Button className='w-100' style={{height: '60px'}} variant='primary' onClick={handleShow}>
                    <i className="fas fa-plus me-1 text-gray fw-normal"></i>On-site Book Ticket
                </Button>
            ): (
                <p>Please log in as a Customer to book a ticket.</p>
            )}
        
            <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Book Ticket</Modal.Title>
            </Modal.Header>
            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    
                    <Form.Group className="mb-3" controlId="formBasicEventName">
                        <Form.Label>
                            Ticket options: <br/>
                            <span className='text-muted fst-italic fs-7'>(you can only purchase up to 5 tickets)</span>
                        </Form.Label>
                        {showTicketOptions()}
                    </Form.Group>  
                    {checkTicketingOfficer() }                  
                    <Form.Label>
                        Payment method:
                    </Form.Label>
                    <br/>
                    
                    <div className="row">
                        <div className="col-sm-6 mb-2">
                            <Button
                                variant={formData.paymentMethod === 'wallet' ? 'danger' : 'outline-secondary'}
                                className="w-100 py-3 rounded"
                                onClick={() => handleInputChange({ target: { name: 'paymentMethod', value: 'wallet' } })}
                            > 
                            
                            {userData && userData.role === 'Ticketing_Officer' ? 'Cash' : 'Wallet'}
                            </Button>
                        </div>
                        <div className="col-sm-6 mb-2">
                            <Button
                                variant={formData.paymentMethod === 'card' ? 'info' : 'outline-secondary'}
                                className="w-100 py-3 rounded"
                                onClick={() => handleInputChange({ target: { name: 'paymentMethod', value: 'card' } })}
                            > Card Payment
                            </Button>
                        </div>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button type="submit" className="btn btn-primary mt-1 w-100">Submit</Button>
                </Modal.Footer>
            </Form>
            </Modal>

            <Modal show={show2} onHide={handleClose2}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Ticket Result</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{results.message}</p>
                </Modal.Body>
            </Modal>
        </>
        
    )
}
export default BookTicket;