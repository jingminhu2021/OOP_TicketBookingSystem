import React from "react";

function Navbar(){
    return(
        <div> 
            <header class="header navbar-area">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col-lg-12">
                            <div class="nav-inner">
                        
                                <nav class="navbar navbar-expand-lg">
                                    <a class="navbar-brand" href="index.html">
                                        <img src="./logo/logo.svg" alt="Logo"></img>
                                    </a>
                                    <button class="navbar-toggler mobile-menu-btn" type="button" data-bs-toggle="collapse"
                                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                                        aria-expanded="false" aria-label="Toggle navigation">
                                        <span class="toggler-icon"></span>
                                        <span class="toggler-icon"></span>
                                        <span class="toggler-icon"></span>
                                    </button>
                                    <div class="collapse navbar-collapse sub-menu-bar" id="navbarSupportedContent">
                                        <ul id="nav" class="navbar-nav ms-auto">
                                            <li class="nav-item">
                                                <a href="index.html" class="active" aria-label="Toggle navigation">Home</a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="dd-menu collapsed" href="javascript:void(0)" data-bs-toggle="collapse"
                                                    data-bs-target="#submenu-1-1" aria-controls="navbarSupportedContent"
                                                    aria-expanded="false" aria-label="Toggle navigation">Pages</a>
                                                <ul class="sub-menu collapse" id="submenu-1-1">
                                                    <li class="nav-item"><a href="about-us.html">About Us</a></li>
                                                    <li class="nav-item"><a href="gallery.html">Gallery</a></li>
                                                    <li class="nav-item"><a href="pricing.html">Pricing</a></li>
                                                    <li class="nav-item"><a href="sponsors.html">Sponsors</a></li>
                                                    <li class="nav-item"><a href="mail-success.html">Mail Success</a></li>
                                                    <li class="nav-item"><a href="404.html">404 Error</a></li>
                                                </ul>
                                            </li>
                                            <li class="nav-item">
                                                <a href="schedule.html" aria-label="Toggle navigation">Schedule</a>
                                            </li>
                                            <li class="nav-item">
                                                <a href="speakers.html" aria-label="Toggle navigation">Speakers</a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="dd-menu collapsed" href="javascript:void(0)" data-bs-toggle="collapse"
                                                    data-bs-target="#submenu-1-2" aria-controls="navbarSupportedContent"
                                                    aria-expanded="false" aria-label="Toggle navigation">Blog</a>
                                                <ul class="sub-menu collapse" id="submenu-1-2">
                                                    <li class="nav-item"><a href="blog-grid.html">Blog Grid</a>
                                                    </li>
                                                    <li class="nav-item"><a href="blog-single.html">Blog Single</a></li>
                                                </ul>
                                            </li>
                                            <li class="nav-item">
                                                <a href="contact.html" aria-label="Toggle navigation">Contact</a>
                                            </li>
                                        </ul>
                                    </div> 
                                    <div class="button">
                                        <a href="pricing.html" class="btn">Get Tickets<i class="lni lni-ticket"></i></a>
                                    </div>
                                </nav>
                                
                            </div>
                        </div>
                    </div> 
                </div> 
            </header>
        </div>
    )
}

export default Navbar;