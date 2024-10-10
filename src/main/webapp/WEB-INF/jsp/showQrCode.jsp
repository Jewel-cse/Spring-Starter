<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>QR Code for 2FA</title>
</head>
<body>

<h2>Scan this QR code to enable Two-Factor Authentication</h2>

<!-- Display the QR code image -->
<img src="${qrCodeUrl}" alt="QR Code" width="400" height="400" />

</body>
</html>
