const db = require('../db')

class PlaceController {
	//http://localhost:3000/places/list	?filter=default
	async listOfPlaces(req, res) {
		try {
			const { filter } = req.query;

			const { user_id } = req.body; 

			console.log(`List for -> ${user_id}`);
			const result = (user_id) ?
				await db.query(`SELECT places.*,
    CASE
        WHEN favorites_places.is_favorite IS NOT NULL THEN favorites_places.is_favorite
        ELSE 0
    END AS is_favorite,
    ARRAY_AGG(images.image_url) AS images
FROM
    places
LEFT JOIN
    favorites_places ON favorites_places.place_id = places.id AND favorites_places.user_id = ${user_id}
Left JOIN
	images ON images.place_id = places.id
GROUP BY 
	places.id, favorites_places.is_favorite;`)
			 : 
			 	await db.query(`
			 		SELECT places.*, 0 AS is_favorite,
    				ARRAY_AGG(images.image_url) AS images 
    				FROM places 
			 		Left JOIN images ON images.place_id = places.id
					GROUP BY places.id;`);

			res.json(result.rows);
		} catch(e) {
			console.log(e.message);
			res.status(500).json(e.message);
		}
	}
}

module.exports = new PlaceController();
/*
[
    {
        "id": 1,
        "name": "Парк Рудаки",
        "latitude": 38.576290022103,
        "longitude": 68.7847677535899,
        "description": "Гор",
        "category": "",
        "is_favorite": 0,
        [
        	"/photos/presidential_palace_1.jpg",
        	"/photos/park_rudaki_2.jpg",
        	"/photos/park_rudaki_3.jpg"
        ]
    },
    {
        "id": 2,
        "name": "Дворец Нации",
        "latitude": 38.57625532932752,
        "longitude": 68.77876043971129,
        "description": "Двор",
        "category": "",
        "is_favorite": 0,
        [
        	"/photos/presidential_palace_1.jpg",
        	"/photos/presidential_palace_.jpg"
        ]
    }
]*/