package com.epam.esm.repository.audit;

import java.util.Date;

import javax.annotation.PreDestroy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {
    

    @PrePersist
    private void persistAudit(Object o){
        System.out.println("Persist audit triggered:" + o + "|timestamp:"+ new Date().getTime());
    }

    @PreUpdate
    private void updateAudit(Object o){
        System.out.println("Update audit triggered:" + o+ "|timestamp:"+ new Date().getTime());
    }

    @PreRemove
    private void removeAudit(Object o){
        System.out.println("Remove audit triggered:" + o+ "|timestamp:"+ new Date().getTime());
    }

    @PreDestroy
    private void destroyAudit(Object o){
        System.out.println("Destroy audit triggered:" + o+ "|timestamp:"+ new Date().getTime());
    }

}
