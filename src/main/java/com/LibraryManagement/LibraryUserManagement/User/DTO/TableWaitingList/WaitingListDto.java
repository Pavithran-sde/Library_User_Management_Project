package com.LibraryManagement.LibraryUserManagement.User.DTO.TableWaitingList;

import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingListDto {
        private long userId;

        private WaitingListEnum waitingListStatus;

        private LocalDateTime WLentryTime;

        private LocalDateTime WLexitTime;
}
