local function init(opts)
    if opts.is_master then


        local kv_space = box.schema.space.create('KV', {
                    if_not_exists = true,
                    format = {
                        { 'bucket_id', 'unsigned' },
                        {name = 'key', type = 'string'},
                        {name = 'value', type = 'string', is_nullable = true}
                    }
            })
         kv_space:create_index('kv_index', {
                 parts = { { field = "key", type = "string" } },
                 if_not_exists = true
             })

    end
end

local role_name = "storage"

return {
    dependencies = { 'cartridge.roles.vshard-storage' },
    role_name = role_name,
    init = init
}
