Для запуска приложения в dockerнеобходимо выполнить следующие шаги:

1. Выполнить сборку 
```
sh mvnw clean package
```

2. Запустить контейнеры

```shell
docker-compose up --build --detach
```   

3. Подключиться к консоли одного из контейнеров, например, к `tarantool-server1`

```shell
docker-compose exec tarantool-server1 /bin/bash
```

4. Подключитесь  к экземпляру Tarantool.

```shell
tarantoolctl connect http://admin:admin@127.0.0.1:3301
```

5. Вследующий скрипт
   Он выполнит объединение экземпляров Tarantool в replica sets и назначение экземплярам определённых ролей.

```lua
cartridge = require('cartridge')
replicasets = { {
                    alias = 'router1',
                    roles = { 'router', 'vshard-router', 'failover-coordinator' },
                    join_servers = { { uri = 'tarantool-server1:3301' } }
                },

                 {
                    alias = 'storage1',
                    roles = { 'storage', 'vshard-storage' },
                    join_servers = { { uri = 'tarantool-server4:3301' },
                                     { uri = 'tarantool-server5:3301' },
                                     { uri = 'tarantool-server6:3301' } }
                }

                }
cartridge.admin_edit_topology({ replicasets = replicasets })
```

Возможно отключение консоли

И ещё один скрипт

```
cartridge.admin_bootstrap_vshard()
```
6. Админка Tarantool Cartridge http://localhost:8081/admin или
   http://192.168.99.100:8081/admin.
   
   