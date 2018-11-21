expirationd = require('expirationd')

box.cfg{
    listen = 3301
}

box.schema.user.create('contract_user', {password = 'secret'})

schem = box.schema.space.create("contract-schema")

schem:format({
    {name = 'id', type = 'unsigned'},
    {name = 'title', type = 'string'},
    {name = 'duration', type = 'unsigned'}
})

job_name = 'clean_contracts'

function is_expired(args, tuple)
    return tuple[2] == 1000 * box.space.contract-schema:count(tuple[1], {iterator='GE'})
end

function delete_tuple(space_id, args, tuple)
    box.space[space_id]:delete{tuple[1]}
end

expirationd.start(job_name, schem.id, is_expired, {
    process_expired_tuple = delete_tuple, args = nil,
    tuples_per_iteration = 50, full_scan_time = 3600
})