<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exciting New Offer from VECTOR!</title>
    <style>
        /* General Styling */
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background-color: #f2f2f7;
            margin: 0;
            padding: 0;
        }

        .email-container {
            width: 100%;
            max-width: 600px;
            margin: 40px auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .email-header {
            text-align: center;
            padding: 20px;
            background-color: #007bff;
            color: #ffffff;
            border-radius: 8px 8px 0 0;
        }

        .email-content {
            padding: 20px;
            line-height: 1.6;
            color: #333333;
        }

        .product-highlight {
            text-align: center;
            margin: 20px 0;
        }

        .product-image {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            margin: 20px 0;
        }

        .highlighted-features {
            list-style: none;
            padding: 0;
            margin: 20px 0;
        }

        .highlighted-features li {
            background-color: #f9f9f9;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            color: #555555;
        }

        .call-to-action {
            text-align: center;
            margin: 20px 0;
        }

        .cta-button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #28a745;
            color: #ffffff;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .cta-button:hover {
            background-color: #218838;
        }

        /* Footer Styling */
        .email-footer {
            text-align: center;
            padding: 20px;
            font-size: 12px;
            color: #777777;
            border-top: 1px solid #e0e0e0;
        }

        /* Social Links Styling */
        .social-links {
            margin-top: 20px;
            text-align: center;
        }

        .social-link {
            display: inline-block;
            margin: 0 10px;
        }

        .social-link img {
            width: 24px;
            height: 24px;
            transition: transform 0.3s ease;
        }

        .social-link img:hover {
            transform: scale(1.1);
        }
    </style>
</head>
<body>
<div class="email-container">
    <!-- Header Section -->
    <div class="email-header">
        <h1>Special Offer Just for You, ${userName}!</h1>
    </div>

    <!-- Content Section -->
    <div class="email-content">
        <p>Hi ${userName},</p>
        <p>We’re excited to announce our latest product, designed to help you.</p>

        <!-- Product Highlight Section -->
        <div class="product-highlight">
            <img src="${productImageUrl}" alt="Product Image" class="product-image">
        </div>

        <!-- Key Features -->
        <p>Here’s what makes this product special:</p>
        <ul class="highlighted-features">
            <#list features as feature>
                <li>${feature}</li>
            </#list>
        </ul>

        <!-- Call to Action -->
        <div class="call-to-action">
            <a href="${ctaLink}" class="cta-button">Get Started Today</a>
        </div>
    </div>

    <!-- Social Links Section -->
    <div class="social-links">
        <a href="https://www.facebook.com/yourcompany" class="social-link" target="_blank">
            <img src="cid:facebook" alt="Facebook">
        </a>
        <a href="https://twitter.com/yourcompany" class="social-link" target="_blank">
            <img src="cid:twitter" alt="Twitter">
        </a>
        <a href="https://www.instagram.com/yourcompany" class="social-link" target="_blank">
            <img src="cid:instagram" alt="Instagram">
        </a>
        <a href="https://www.linkedin.com/company/yourcompany" class="social-link" target="_blank">
            <img src="cid:linkedin" alt="LinkedIn">
        </a>
    </div>

    <!-- Footer Section -->
    <div class="email-footer">
        <p>&copy; ${year} VECTOR. All rights reserved.</p>
        <p>If you would like to unsubscribe, <a href="/#">click here</a>.</p>
    </div>
</div>
</body>
</html>
