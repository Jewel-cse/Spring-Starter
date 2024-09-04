<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Your Password</title>
    <style>
        /* General Styling */
        body {
            font-family: Arial, sans-serif;
            background-color: #ccccff;
            margin: 0;
            padding: 0;
        }

        .email-container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            animation: fadeIn 1.5s ease-in-out; /* Animation for container */
        }

        .email-header {
            text-align: center;
            padding: 20px;
            background-color: #007bff;
            color: #ffffff;
            border-radius: 8px 8px 0 0;
            animation: slideDown 1s ease-out; /* Animation for header */
        }

        .email-content {
            padding: 20px;
            line-height: 1.6;
            color: #888888;
        }

        .email-footer {
            text-align: center;
            padding: 20px;
            font-size: 12px;
            color: #777777;
            border-top: 1px solid #e0e0e0;
        }

        /* Button Styling */
        .reset-button {
            display: inline-block;
            margin: 20px auto;
            padding: 12px 24px;
            background-color: #007bff;
            color: #ffffff;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s ease; /* Animation for button hover */
        }

        .reset-button:hover {
            background-color: #0056b3;
            text-decoration-color: aliceblue;
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
            transition: transform 0.3s ease; /* Animation for social icons */
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
<body>
<div class="email-container">
    <div class="email-header">
        <h1>Password Reset Request</h1>
    </div>
    <div style="max-width: 500px; align-items: center"><img alt="Wrong Password Animation" height="auto" src="cid:GIF_password" style="display: block; height: auto; border: 0; width: 100%;" title="Wrong Password Animation" width="500"/></div>

    <div class="email-content">
        <p>Hello, ${userName}!</p>
        <p>We received a request to reset your password. Please click the button below to set a new password:</p>
        <p style="text-align: center;">
            <a href="${resetPasswordUrl}" class="reset-button">Reset Password</a>
        </p>
        <p>If you did not request a password reset, please ignore this email. Your password will remain unchanged.</p>
        <p>Thank you,<br>VECTOR</p>
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
