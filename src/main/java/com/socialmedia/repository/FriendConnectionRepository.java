package com.socialmedia.repository;

import com.socialmedia.entity.ConnectionStatus;
import com.socialmedia.entity.FriendConnection;
import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendConnectionRepository extends JpaRepository<FriendConnection,Long> {
boolean existsByRequesterAndReceiver(User requester,User receiver);

List<FriendConnection> findByReceiverAndStatus(User receiver, ConnectionStatus status);

    @Query("SELECT f FROM FriendConnection f WHERE (f.requester = :user OR f.receiver = :user) AND f.status = com.socialmedia.entity.ConnectionStatus.ACCEPTED")
List<FriendConnection> findAcceptedConnections(@Param("user") User user);

}
