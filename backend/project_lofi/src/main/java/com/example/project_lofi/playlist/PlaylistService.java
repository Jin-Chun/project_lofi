package com.example.project_lofi.playlist;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.GeneralUtils;
import com.example.project_lofi.constants.PL_PlaylistStatus;
import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.lofipool.LofiPool;
import com.example.project_lofi.lofipool.LofiPoolService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class PlaylistService extends AbstractService{
    private final PlaylistRepository playlistRepository;
    private final LofiPoolService lofiPoolService;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, LofiPoolService lofiPoolService){
        this.playlistRepository = playlistRepository;
        this.lofiPoolService = lofiPoolService;
    }

    public List<Playlist> getAllPlaylists(){
        return this.playlistRepository.findAll();
    }

    public Playlist getPlaylistById(long playlistId){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlistId);
        if (existingPlaylist.isPresent()){
            return existingPlaylist.get();
        } else {
            String message = String.format("No such a Playlist identifier %d is found", playlistId);
            log.error(message, playlistId);
            throw new IllegalArgumentException(message);
        }
    }

    public Playlist getPlaylistByName(String playlistName){
        Optional<Playlist> optionalPlaylist = this.playlistRepository.findPlaylistByName(playlistName);
        if (optionalPlaylist.isPresent()){
            return optionalPlaylist.get();
        } else {
            String message = String.format("No such a playlist name %s is found", playlistName);
            log.error(message, playlistName);
            return null;
        }
    }

    public Playlist savePlaylist(Playlist playlist){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findPlaylistByName(playlist.getPlaylistName());

        if(!existingPlaylist.isPresent()){
            return this.playlistRepository.save(playlist);
        } else {   
            String message = String.format("Cannot save the same Playlist name %s", playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
    }

    public Playlist updatePlaylist(Playlist playlist){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlist.getPlaylistId());

        if(existingPlaylist.isPresent()){
            return this.playlistRepository.save(playlist);
        } else {
            String message = String.format("No such a playlist info. Cannot update the Playlist of playlistId %d, playlistName %s", 
            playlist.getPlaylistId(), playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
    }

    public Playlist deletePlaylistById(long playlistId){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlistId);

        if(existingPlaylist.isPresent()){
            Playlist playlistToBeDeleted = existingPlaylist.get();
            this.playlistRepository.delete(existingPlaylist.get());
            return playlistToBeDeleted;
        } else {
            String message = String.format("No such a playlist info. Cannot delete the playlist, the given playlist identifier: %d", 
                playlistId);
            log.error(message, playlistId);
            throw new IllegalArgumentException(message);
        }
    }

    public Playlist releasePlaylist(long playlistId){
        checkId(playlistId, "playlistId");

        Playlist retrievedPlaylist = this.getPlaylistById(playlistId);
        checkNull(retrievedPlaylist, "retrievedPlaylist");

        retrievedPlaylist.setPlaylistStatus(PL_PlaylistStatus.RELEASED);
        return this.updatePlaylist(retrievedPlaylist);
    }

    public List<Playlist> getReleasedPlaylists(){
        List<Playlist> releasedPlaylists = this.playlistRepository.findPlaylistsByStatus(PL_PlaylistStatus.RELEASED);

        if(releasedPlaylists == null || releasedPlaylists.isEmpty()){
            log.info("There are no released playlists");
        }
        
        return releasedPlaylists;
    }

    public Playlist pullLofiesFromLofiPool(long lofiPoolId, int numOfLofies, long playlistId){
        checkId(lofiPoolId, "lofiPool");
        checkId(playlistId, "playlistId");
        checkMinNum(numOfLofies);

        // 1. validate the given lofi pool
        LofiPool retrievedLofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);
        checkNull(retrievedLofiPool, "retrievedLofiPool");

        // 2. validate the given playlistId
        Playlist retrievedPlaylist = this.getPlaylistById(playlistId);
        checkNull(retrievedPlaylist, "playlist");

        // 3. add the given number of lofies from the lofi pool into the playlist's lofies
        if (numOfLofies > retrievedLofiPool.getPoolLofies().size()){
            // if the given number exceeds the maximum number of lofies in the lofiPool, throws error
            String message = String.format("The given number(numOfLofies: %d) of lofies that should be retrieved exceeds the number of lofies in the pool(lofiPoolId: %d)", numOfLofies, lofiPoolId);
            log.error(message);
            throw new IllegalArgumentException(message);

        } else if (numOfLofies == retrievedLofiPool.getPoolLofies().size()){
            // if the given number is the same as the size of lofies in lofiPool, add all to playlist's lofies
            for (Lofi lofi : retrievedLofiPool.getPoolLofies()){
                retrievedPlaylist.getPlaylistLofies().add(lofi);
            }

        } else {
            // generate random numbers as many as the given number in range of 0 to the size of lofies in lofiPool
            Set<Integer> randomNums = GeneralUtils.generateUniqueRandomNumbers(numOfLofies, 0, retrievedLofiPool.getPoolLofies().size());

            // retrieve and add the lofies from the lofiPool by using the random numbers as indexes
            for(int index : randomNums){
                Lofi lofi = retrievedLofiPool.getPoolLofies().get(index);
                retrievedPlaylist.getPlaylistLofies().add(lofi);
            }
        }

        // 7. return the updated playlist
        Playlist updatedPlaylist = this.updatePlaylist(retrievedPlaylist);
        return updatedPlaylist;
    }
}
