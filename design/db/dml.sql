use project_lofi;
-- insert user data (user_id, user_created, user_name, user_password, user_type, user_updated, user_version)
insert into user values(1, CURRENT_TIMESTAMP(), "JMIG@U1", "PASSWORD1", "01", CURRENT_TIMESTAMP(), 1);
insert into user values(2, CURRENT_TIMESTAMP(), "JMIG@W1", "PASSWORD1", "02", CURRENT_TIMESTAMP(), 1);

-- insert lofi data (lofi_id, lofi_author, lofi_location, lofi_name, lofi_playtime, lofi_type)
insert into lofi values(1, "Soundroll", "/assets/lofi/1-all_the_things_you_love-soundroll-315.mp3", "All the things you love", "3:15", "02");
insert into lofi values(2, "Sensho", "/assets/lofi/2-coffee_break-sensho-152.mp3", "Coffee break", "1:52", "02");
insert into lofi values(3, "Soundroll", "/assets/lofi/3-coffee_shop-soundroll-249.mp3", "Coffee shop", "2:49", "02");
insert into lofi values(4, "Tatami", "/assets/lofi/4-come_back-tatami-155.mp3", "Come back", "1:55", "01");
insert into lofi values(5, "Mountainner", "/assets/lofi/5-cozy_mood-mountainner-128.mp3", "Cozy mood", "1:28", "05");
insert into lofi values(6, "Jonny boyle", "/assets/lofi/6-easy_does_it-jonny_boyle-228.mp3", "Easy does it", "2:28", "03");
insert into lofi values(7, "Soundroll", "/assets/lofi/7-forgotten-soundroll-217.mp3", "Forgotten", "2:17", "01");
insert into lofi values(8, "Pecan pie", "/assets/lofi/8-important_to_you-pecan_pie-206.mp3", "Important to you", "2:06", "04");
insert into lofi values(9, "Soundroll", "/assets/lofi/9-lofi_love-soundroll-247.mp3", "Lofi love", "2:47", "03");
insert into lofi values(10, "Pryces", "/assets/lofi/10-music_is-pryces-254.mp3", "Music is", "2:54", "01");

-- insert lofi pool data (lofi_pool_id, lofi_pool_genre, lofi_pool_name)
insert into lofi_pool values(1, "01", "All lofies");
insert into lofi_pool values(2, "02", "Lofi & Jazz");
insert into lofi_pool values(3, "03", "Lofi & Chill");

-- insert playlist data (playlist_id, playlist_created, playlist_genre, playlist_name, playlist_status, playlist_updated, playlist_version, user_user_id)
insert into playlist values(1, CURRENT_TIMESTAMP(), "01", "Test playlist 1", "01", CURRENT_TIMESTAMP(), 1, 1);
insert into playlist values(2, CURRENT_TIMESTAMP(), "02", "Test playlist 2", "01", CURRENT_TIMESTAMP(), 1, 1);
insert into playlist values(3, CURRENT_TIMESTAMP(), "03", "Test playlist 3", "01", CURRENT_TIMESTAMP(), 1, 2);

-- insert playlist_assignment (lofi_id, playlist_id, playlist_lofi_assignment_time)
insert into playlist_lofi_assignment values(1, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(2, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(3, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(4, 1, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(10, 2, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(9, 2, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(1, 3, CURRENT_TIMESTAMP());
insert into playlist_lofi_assignment values(10, 3, CURRENT_TIMESTAMP());

-- insert lofi_pool_assignment (lofi_pool_id, lofi_id)
insert into lofi_pool_assignment values(1, 1);
insert into lofi_pool_assignment values(1, 2);
insert into lofi_pool_assignment values(1, 3);
insert into lofi_pool_assignment values(1, 4);
insert into lofi_pool_assignment values(1, 5);
insert into lofi_pool_assignment values(1, 6);
insert into lofi_pool_assignment values(1, 7);
insert into lofi_pool_assignment values(1, 8);
insert into lofi_pool_assignment values(1, 9);
insert into lofi_pool_assignment values(1, 10);
insert into lofi_pool_assignment values(2, 1);
insert into lofi_pool_assignment values(2, 2);
insert into lofi_pool_assignment values(2, 3);
insert into lofi_pool_assignment values(3, 6);
insert into lofi_pool_assignment values(3, 9);