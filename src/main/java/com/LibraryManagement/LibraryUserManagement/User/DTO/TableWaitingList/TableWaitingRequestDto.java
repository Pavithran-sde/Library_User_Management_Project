package com.LibraryManagement.LibraryUserManagement.User.DTO.TableWaitingList;


import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableWaitingRequestDto {

    private long userId;

    @NotNull
    private LocalDateTime WLexitTime = LocalDateTime.now();

    private WaitingListEnum statusEnum =  WaitingListEnum.CANCELED;
}
