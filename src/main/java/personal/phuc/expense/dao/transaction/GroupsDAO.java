package personal.phuc.expense.dao.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import personal.phuc.expense.entity.Groups;

import java.util.List;

@Repository
public interface GroupsDAO extends JpaRepository<Groups, Integer> {

    @Query(value = "Select g from Group g where g.appUser.id=?1 order by g.name  ")
    List<Groups> findGroups(Integer userId);

    @Query(value = "select g from Group g where g.id=?1 and g.appUser.id =?2")
    Groups findGroup(Integer id, Integer userId);

    @Transactional
    @Modifying
    @Query(value = "Delete from Group g where g.id=?1 and g.appUser.id=?2")
    void deleteByIdAndUserId(Integer id, Integer userId);
}
