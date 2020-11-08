db.createUser(
	{
		user: "robert",
		pwd: "robert",
		roles: [
			{
				role: "readWrite",
				db: "bank"
			}
		]
	}
)