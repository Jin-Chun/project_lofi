-- insert user data (user_id, user_created, user_name, user_password, user_type, user_updated, user_version)
insert into user values(1, CURRENT_TIMESTAMP(), "JMIG@W1", "PASSWORD1", "01", CURRENT_TIMESTAMP(), 1);

-- insert lofi data (lofi_id, lofi_author, lofi_location, lofi_name, lofi_playtime, lofi_type)
insert into lofi values(1, "Author1", "/resource/lofi/jazz", "sample2", "4:03", "01");

-- insert lofi pool data (lofi_pool_id, lofi_pool_genre, lofi_pool_name)
insert into lofi_pool values(1, "02", "sample_lofi_pool1");

-- insert playlist data (playlist_id, playlist_created, playlist_genre, playlist_name, playlist_status, playlist_updated, playlist_version, user_user_id)
insert into playlist values(1, CURRENT_TIMESTAMP(), "02", "Sample_playlist1", "01", CURRENT_TIMESTAMP(), 1, 1);

-- insert playlist_assignment (playlist_id, lofi_id)
insert into playlist_assignment values(1, 1);

-- insert lofi_pool_assignment (lofi_pool_id, lofi_id)
insert into lofi_pool_assignment values(1, 1);