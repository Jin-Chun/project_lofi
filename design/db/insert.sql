use project_lofi;
-- insert user data (user_id, user_created, user_name, user_password, user_type, user_updated, user_version)
insert into user values(1, CURRENT_TIMESTAMP(), "JMIG@U1", "PASSWORD1", "01", CURRENT_TIMESTAMP(), 1);

-- insert lofi data (lofi_id, lofi_author, lofi_location, lofi_name, lofi_playtime, lofi_type)
insert into lofi values(1, "Author1", "/resource/lofi/jazz/1", "sample1", "4:01", "01");
insert into lofi values(2, "Author1", "/resource/lofi/jazz/2", "sample2", "4:02", "01");
insert into lofi values(3, "Author1", "/resource/lofi/jazz/3", "sample3", "4:03", "01");
insert into lofi values(4, "Author1", "/resource/lofi/jazz/4", "sample4", "4:04", "01");

-- insert lofi pool data (lofi_pool_id, lofi_pool_genre, lofi_pool_name)
insert into lofi_pool values(1, "02", "sample_lofi_pool1");

-- insert playlist data (playlist_id, playlist_created, playlist_genre, playlist_name, playlist_status, playlist_updated, playlist_version, user_user_id)
insert into playlist values(1, CURRENT_TIMESTAMP(), "02", "Sample_playlist1", "01", CURRENT_TIMESTAMP(), 1, 1);
insert into playlist values(2, CURRENT_TIMESTAMP(), "02", "Sample_playlist1", "01", CURRENT_TIMESTAMP(), 1, 1);
insert into playlist values(3, CURRENT_TIMESTAMP(), "02", "Sample_playlist2", "01", CURRENT_TIMESTAMP(), 1, 1);

-- insert playlist_assignment (lofi_id, playlist_id, playlist_lofi_assignment_time)
insert into playlist_lofi_assignment values(1, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(2, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(3, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(4, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(1, 2, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(2, 2, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(3, 3, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(4, 3, CURRENT_TIMESTAMP());

-- insert lofi_pool_assignment (lofi_pool_id, lofi_id)
insert into lofi_pool_assignment values(1, 1);
insert into lofi_pool_assignment values(1, 2);
insert into lofi_pool_assignment values(1, 3);
insert into lofi_pool_assignment values(1, 4);