function getEnv(envVar) {
    run("sh", "-c", `printenv ${envVar} >/tmp/${envVar}.txt`);
    return cat(`/tmp/${envVar}.txt`)
}

rs.initiate();
use admin;
db.createUser(
    {
        "user": getEnv('MONGO_INITDB_ROOT_USERNAME'),
        "pwd": getEnv('MONGO_INITDB_ROOT_PASSWORD'),
        roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
    }
)
