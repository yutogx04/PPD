# install maven and mongodb and mongosh
```

    sudo apt install mongodb mongosh maven

```
# run the database
```

    systemctl start mongodb

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

    mvn spring-boot:run

```
the interface is at ```localhost:8080/Examples```

# run all the test
```

    mvn test

```
