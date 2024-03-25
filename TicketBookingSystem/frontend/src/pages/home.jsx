import React from 'react';
import Navbar from '../components/navbar';
import UpdateEvent from '../components/updateEvent';

import image from "../img/product-5.jpg";

function Home() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);

    return (
            <div>
            <Navbar />
            {UpdateEvent(urlParams.get('id'))}
            <div className="modal fade" id="productView" tabIndex="-1">
                <div className="modal-dialog modal-lg modal-dialog-centered">
                <div className="modal-content overflow-hidden border-0">
                    <button className="btn-close p-4 position-absolute top-0 end-0 z-index-20 shadow-0" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                    <div className="modal-body p-0">
                    <div className="row align-items-stretch">
                        <div className="col-lg-6 p-lg-0"><a className="glightbox product-view d-block h-100 bg-cover bg-center" 
                            style={{backgroundImage: "url(" + image + ")"}} href="img/product-5.jpg" data-gallery="gallery1" data-glightbox="Red digital smartwatch"></a>
                        <div className="col-lg-6">
                        <div className="p-4 my-md-4">
                            <ul className="list-inline mb-2">
                            <li className="list-inline-item m-0"><i className="fas fa-star small text-warning"></i></li>
                            <li className="list-inline-item m-0 1"><i className="fas fa-star small text-warning"></i></li>
                            <li className="list-inline-item m-0 2"><i className="fas fa-star small text-warning"></i></li>
                            <li className="list-inline-item m-0 3"><i className="fas fa-star small text-warning"></i></li>
                            <li className="list-inline-item m-0 4"><i className="fas fa-star small text-warning"></i></li>
                            </ul>
                            <h2 className="h4">Red digital smartwatch</h2>
                            <p className="text-muted">$250</p>
                            <p className="text-sm mb-4">Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ut ullamcorper leo, eget euismod orci. Cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus. Vestibulum ultricies aliquam convallis.</p>
                            <div className="row align-items-stretch mb-4 gx-0">
                            <div className="col-sm-7">
                                <div className="border d-flex align-items-center justify-content-between py-1 px-3"><span className="small text-uppercase text-gray mr-4 no-select">Quantity</span>
                                <div className="quantity">
                                    <button className="dec-btn p-0"><i className="fas fa-caret-left"></i></button>
                                    <input className="form-control border-0 shadow-0 p-0" type="text" value="1"></input>
                                    <button className="inc-btn p-0"><i className="fas fa-caret-right"></i></button>
                                </div>
                                </div>
                            </div>
                            <div className="col-sm-5"><a className="btn btn-dark btn-sm w-100 h-100 d-flex align-items-center justify-content-center px-0" href="cart.html">Add to cart</a></div>
                            </div><a className="btn btn-link text-dark text-decoration-none p-0" href="#!"><i className="far fa-heart me-2"></i>Add to wish list</a>
                        </div>
                        </div>
                    </div>
                    </div>
                </div>
                </div>
            </div>
            </div>
        </div>
    )
}

export default Home;