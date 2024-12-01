local vshard = require('vshard')
local log = require('log')

local function init(opts)
    return true
end

local function stop()
end

local function validate_config(conf_new, conf_old)
    return true
end

local function apply_config(conf, opts)
    return true
end

--- Создание объекта в kv.
function create_kv_object(key, value)
    log.info('create_kv_object ' .. key)
    log.info('create_kv_object value' .. type(value))

    local bucket_id = vshard.router.bucket_id(key);

    local obj, err = vshard.router.callrw(bucket_id, 'box.space.KV:insert', {
        { bucket_id, key, value }
    })

    if err ~= nil then
        log.error('error in create_kv_object')
        log.error(err)
        return nil
    end

    return obj
end


function get_kv_object_by_key(key)
    log.debug('get_kv_object_by_key ' .. key)

    local bucket_id = vshard.router.bucket_id(key);

    local objects, err = vshard.router.callbro(bucket_id, 'box.space.KV:select', { key })

    if err ~= nil then
        log.error(err)
        return nil
    end

    return objects[1]
end

function delete_kv_object_by_key(key)
    log.debug('delete_kv_object_by_key ' .. key)

    local bucket_id = vshard.router.bucket_id(key);

    local obj, err = vshard.router.callrw(bucket_id, 'box.space.KV:delete', { key })

    if err ~= nil then
        log.error(err)
        return nil
    end

    return obj
end


--- Обновление учётной записи пользователя. Поиск учётной записи производится по UUID учётной записи.
function update_kv_object(key, value)
    log.debug('update_kv_object ' .. key .. '|' .. value)

    local obj = get_kv_object_by_key(key)
    log.info(user)
    if obj ~= nil then

        local updated = update_kv_object_internal(obj[1], key, value)

        return updated

    end

    return nil
end


function update_kv_object_internal(bucket_id, key, value)
    log.debug('update_kv_object_internal ' .. bucket_id .. '|' .. key .. '|' .. value)
    local obj, err = vshard.router.callrw(bucket_id, 'box.space.KV:update', { key, {
        { '=', 2, key },
        { '=', 3, value }
    } })

    if err ~= nil then
        log.error(err)
        return nil, err
    end

    return obj
end

-- Функция для получения количества объектов в space `KV`
function get_count_of_kv_space()
    local bucket_id = 0  -- Получите любой валидный bucket_id, например, 0 или любой другой
    local count, err = vshard.router.callbro(bucket_id, 'box.space.KV:count', {})

    if err ~= nil then
        log.error(err)
        return nil
    end

    return count
end

local role_name = "router"

return {
    dependencies = { 'cartridge.roles.vshard-router' },
    role_name = role_name,
    init = init,
    stop = stop,
    validate_config = validate_config,
    apply_config = apply_config
}
