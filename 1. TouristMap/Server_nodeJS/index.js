const express = require('express');
const app = express();

app.use(express.json());


app.use('/photos', express.static('photos'));

app.use('/auth', require('./routes/auth.routes'));
app.use('/place', require('./routes/place.routes'));

const port = 3000;
app.listen(port, () => {
	console.log(`Server running on port ${port}`);
});