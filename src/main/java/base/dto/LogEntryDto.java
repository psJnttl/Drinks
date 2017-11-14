package base.dto;

import java.time.LocalDateTime;

public class LogEntryDto {

    private long id;
    private LocalDateTime date;
    private String username;
    private String action;
    private String targetEntity;
    private Long targetId;
    private String targetName;

    private LogEntryDto(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.username = builder.username;
        this.action = builder.action;
        this.targetEntity = builder.targetEntity;
        this.targetId = builder.targetId;
        this.targetName = builder.targetName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private long id;
        private LocalDateTime date;
        private String username;
        private String action;
        private String targetEntity;
        private Long targetId;
        private String targetName;

        private Builder() {
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder targetEntity(String targetEntity) {
            this.targetEntity = targetEntity;
            return this;
        }

        public Builder targetId(Long targetId) {
            this.targetId = targetId;
            return this;
        }

        public Builder targetName(String targetName) {
            this.targetName = targetName;
            return this;
        }

        public LogEntryDto build() {
            return new LogEntryDto(this);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((targetEntity == null) ? 0 : targetEntity.hashCode());
        result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
        result = prime * result + ((targetName == null) ? 0 : targetName.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LogEntryDto other = (LogEntryDto) obj;
        if (action == null) {
            if (other.action != null) return false;
        }
        else if (!action.equals(other.action)) return false;
        if (date == null) {
            if (other.date != null) return false;
        }
        else if (!date.equals(other.date)) return false;
        if (id != other.id) return false;
        if (targetEntity == null) {
            if (other.targetEntity != null) return false;
        }
        else if (!targetEntity.equals(other.targetEntity)) return false;
        if (targetId == null) {
            if (other.targetId != null) return false;
        }
        else if (!targetId.equals(other.targetId)) return false;
        if (targetName == null) {
            if (other.targetName != null) return false;
        }
        else if (!targetName.equals(other.targetName)) return false;
        if (username == null) {
            if (other.username != null) return false;
        }
        else if (!username.equals(other.username)) return false;
        return true;
    }
    
    
}
