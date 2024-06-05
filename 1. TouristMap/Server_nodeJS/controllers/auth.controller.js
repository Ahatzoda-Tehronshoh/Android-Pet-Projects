const db = require('../db')

class AuthorizationController {
	//http://localhost:3000/auth/user
	async getUserById(req, res) {
		try {
			const { userId } = req.body;
			console.log(`User ${userId} tried to logIn!`);
			const result = await db.query('SELECT * FROM users WHERE id = $1;', [userId]);
			res.json(result.rows);
		} catch(e) {
			console.log(e.message);
			res.status(500).json(e.message);
		}
	}

	//http://localhost:3000/auth/register
	async register(req, res) {
		try {
			const {nickName, login, password, country} = req.body;
			console.log(nickName, login, password, country);
			const userId = await db.query('INSERT INTO users(nick_name, login, password, country) values($1, $2, $3, $4) RETURNING id;', [nickName, login, password, country]);
			res.json(userId.rows[0].id);
		} catch(e) {
			console.log(e.message);
			res.status(500).json(e.message);
		}
	}

	//http://localhost:3000/auth/login   ?nickName=Tehron_shoh
	async logIn(req, res) {
		try {
			const {nickName} = req.query;
			const {password} = req.body;	
			console.log(` ${password}`);
			const response = await db.query(
					'SELECT * FROM users WHERE (nick_name = $1 or login = $1) and password = $2',
					[nickName, password]
				);
			res.json(response.rows);
		} catch(e) {
			console.log(e.message);
			res.status(500).json(e.message);
		}
	}
}

module.exports = new AuthorizationController();