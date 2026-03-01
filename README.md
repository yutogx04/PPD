# install maven and mongodb and mongosh
```

    sudo apt install mongodb mongosh maven

```
# run the database
```

    systemctl start mongodb
    systemctl start postgresql

```
# create the user for the data base
```

    mongosh
    db.createUser(
      { 
        user: "app",
        pwd:  "pass",
        roles:
        [
          { role:"readWrite",db:"storage"},
        ] } );

```
# run the app
```

    cd server
    mvn spring-boot:run

```
the interface is at ```localhost:8080/Examples```

# run all the test
```
    
    cd server
    mvn test

```
