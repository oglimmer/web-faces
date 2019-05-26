module.exports = {

	config : {
		SchemaVersion : "1.0.0",
		Name : "Faces"
	},

	software : {

		source : {
			Source : "mvn",
			Artifact : "target/faces.war"
		},

		mysql : {
			Source : "mysql",
			Mysql : {
				Schema : "faces",
				Create : [ "src/db/init-ddl.sql", "src/db/init-data.sql" ],
			}
		},

		tomee : {
			Source : "tomee",
			Deploy : "source",
			EnvVars: [{
		      Source: "mysql",
		      Name: "JAVA_OPTS",
	    	  Value: "-Djdbc/facesdatabase.JdbcUrl=jdbc:mysql://$$VALUE$$/faces?useSSL=false",
    		  DockerOnly: true
			}]
		}
	}

}
