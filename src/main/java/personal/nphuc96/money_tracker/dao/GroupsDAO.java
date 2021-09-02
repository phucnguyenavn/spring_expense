package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.Groups;

import java.util.List;

@Repository
public interface GroupsDAO extends JpaRepository<Groups, Integer> {

    @Query(value = "Select g from Group g where g.appUser.id=?1 order by g.name DESC ")
    List<Groups> findGroupsByUserId(Integer userId);
}