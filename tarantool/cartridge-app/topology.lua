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
return cartridge.admin_edit_topology({ replicasets = replicasets })
