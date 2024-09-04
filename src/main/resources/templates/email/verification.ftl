<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification</title>
    <style>
        /* General Styling */
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background-color: #f7f7f7;
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
            animation: fadeIn 1.5s ease-in-out;
        }

        .email-header {
            text-align: center;
            padding: 20px;
            background-color: #28a745;
            color: #ffffff;
            border-radius: 8px 8px 0 0;
            animation: slideDown 1s ease-out;
        }

        .email-content {
            padding: 20px;
            line-height: 1.6;
            color: #333333;
        }

        .email-footer {
            text-align: center;
            padding: 20px;
            font-size: 12px;
            color: #777777;
            border-top: 1px solid #e0e0e0;
        }

        /* Button Styling */
        .verify-button {
            display: inline-block;
            margin: 20px auto;
            padding: 12px 24px;
            background-color: #28a745;
            color: #ffffff;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .verify-button:hover {
            background-color: #218838;
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

        /* Animations */
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes slideDown {
            from { transform: translateY(-20px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
    </style>
</head>
<body style="background-color: azure">
<div class="email-container">
    <div class="email-header">
        <h1>Welcome to VECTOR!</h1>
    </div>
    <div class="email-content">
        <p>Hi ${userName},</p>
        <p>Thank you for signing up with us! To complete your registration, please verify your email address by clicking the button below:</p>
        <p style="text-align: center; text-decoration-color: #f3f6fe">
            <a href="${verificationLink}" class="verify-button">Verify Your Email</a>
        </p>
        <p>If you did not create an account, no further action is required.</p>
        <p>We are excited to have you on board!<br>Best Regards,<br>VECTOR</p>
    </div>

    <!-- Social Links Section -->
    <div class="social-links">
        <a href="https://www.facebook.com/jewel.rana.26" class="social-link" target="_blank">
            <img src="cid:facebook" alt="Facebook">
        </a>
        <a href="https://twitter.com/yourcompany" class="social-link" target="_blank">
            <img src="cid:twitter" alt="Twitter">
        </a>
        <a href="https://www.instagram.com/__warrior_v0_" class="social-link" target="_blank">
            <img src="cid:instagram" alt="Instagram">
        </a>
        <a href="https://www.linkedin.com/company/yourcompany" class="social-link" target="_blank">
            <img src="cid:linkedin" alt="LinkedIn">
        </a>
    </div>

    <div class="email-footer">
        <p>&copy; ${year} VECTOR. All rights reserved.</p>
    </div>
</div>
</body>
</html>
