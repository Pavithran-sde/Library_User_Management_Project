package com.LibraryManagement.LibraryUserManagement.User.Services;


import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.TableBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {


    @Autowired
    private JavaMailSender mailSender;

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("notification.libraryum@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // 'true' enables HTML

        mailSender.send(mimeMessage);
        System.out.println("HTML Email sent successfully...");
    }


    public void sendTableBookingNotification(TableBooking tableBooking) throws Exception{
        String htmlContent =  String.format("""
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Table Booking Success</title>
                </head>
                <body>
                <table >
                    <tr>
                        <td style = " align="center"; ">
                            <img    style="display: block; max-width: 100%%; height: auto; width: 100%%; border: 0;"
                                    src = "https://raw.githubusercontent.com/Pavithran-sde/skills-github/refs/heads/main/TableBookingConfirmed3.png" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h3>Dear %s,</h3>
                            <p>Thank you for using LibraryUserManagement Application. This Mail is to inform you about your booking for Table</p>
                        </td>
                    </tr>
                    <tr>
                        <td><h4>Your Booking Details</h4>  </td>
                    </tr>
                    <tr>
                        <table border="1px"  cellpadding="5px" cellspacing="5px" style = " text-align : center ">
                            <tr>
                                <th>UserID</th>
                                <th>TableID</th>
                                <th>Floor</th>
                                <th>Wing</th>
                                <th>Reservation Start Time</th>
                            </tr>
                            <tr style = " text-align :center">
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                        </table>
                    </tr>
                </table>
                <h4>Status : %s</h4>
                </body>
                </html>
                """, tableBooking.getUser().getName() ,tableBooking.getUser().getId(),
                    tableBooking.getTables().getId(), tableBooking.getTables().getFloor().getFloorName(),
                tableBooking.getTables().getFloor().getWing(), tableBooking.getReservationStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), tableBooking.getStatus());


        String toEmail = tableBooking.getUser().getEmail();
        String subject =  "Hooray!!, Your Table Has been Booked";
        this.sendHtmlEmail(toEmail, subject, htmlContent);

    }

    public void sendTableClearingNotification(TableBooking tableBooking) throws Exception{
        String htmlContent =  String.format("""
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                   
                </head>
                <body>
                <table >
                    <tr>
                        <td style = " align="center"; ">
                            <img    style="display: block; max-width: 100%%; height: auto; width: 100%%; border: 0;"
                                    src = "https://raw.githubusercontent.com/Pavithran-sde/skills-github/refs/heads/main/TableBookingCleared.png" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h3>Dear %s,</h3>
                            <p>Thank you for using LibraryUserManagement Application. This Mail is to inform you about your Request clearing the booking for Table</p>
                        </td>
                    </tr>
                    <tr>
                        <td><h4>Your Booking Details</h4></td>
                    </tr>
                    <tr>
                        <table border="1px"  cellpadding="5px" cellspacing="5px" >
                            <tr>
                                <th>UserID</th>
                                <th>TableID</th>
                                <th>Floor</th>
                                <th>Wing</th>
                                <th>Reservation Start Time</th>
                                <th>Reservation End Time</th>
                            </tr>
                            <tr style = " text-align :center">
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                            
                        </table>
                    </tr>
                </table>
                <h4>Status : %s</h4>
                </body>
                </html>
                """, tableBooking.getUser().getName() ,tableBooking.getUser().getId(),
                tableBooking.getTables().getId(), tableBooking.getTables().getFloor().getFloorName(),
                tableBooking.getTables().getFloor().getWing(), tableBooking.getReservationStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                tableBooking.getReservationEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), tableBooking.getStatus());

        String toEmail = tableBooking.getUser().getEmail();
        String subject =  "Woah!!, Cleared Your table Booking";
        this.sendHtmlEmail(toEmail, subject, htmlContent);

    }

    public void sendCPBookingNotification(ChargingPortBooking cpbooking) throws Exception{
        String htmlContent =  String.format("""
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                </head>
                <body>
                <table >
                    <tr>
                        <td style = " align="center"; ">
                            <img    style="display: block; max-width: 100%%; height: auto; width: 100%%; border: 0;"
                                    src = "https://raw.githubusercontent.com/Pavithran-sde/skills-github/refs/heads/main/chargingPortBookingConfirmed2.png" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h3>Dear %s,</h3>
                            <p>Thank you for using LibraryUserManagement Application. This Mail is to inform you about your booking for Charging Port</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <h4>Your Booking Details</h4>  
                        </td>
                    </tr>
                    <tr>
                       <td>
                        <table border="1px"  cellpadding="5px" cellspacing="5px" >
                            <tr>
                                <th>UserID</th>
                                <th>ChargingPortID</th>
                                <th>Floor</th>
                                <th>Wing</th>
                                <th>Reservation Time</th>
                            </tr>
                            <tr style = " text-align :center">
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                        </table>
                        </td>
                    </tr>
                    <tr>
                        <h4>RESERVATION END TIME IS SUBJECT TO CHANGES BASED ON DEMAND,<br> YOU WILL BE NOTIFIED WHEN YOUR RESERVATION ENDS. <br> * YOU ARE GUARANTEED TO CHARGE FOR 1 HOUR MINIMUM * </h4>
                    </tr>
                </table>
                <h4>Status : %s</h4>
                </body>
                </html>
                """, cpbooking.getUser().getName(), cpbooking.getUser().getId(),
                     cpbooking.getChargingPort().getId(), cpbooking.getChargingPort().getFloor().getFloorName(),
                    cpbooking.getChargingPort().getFloor().getWing(), cpbooking.getReservationStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), cpbooking.getStatus());


        String toEmail = cpbooking.getUser().getEmail();
        String subject =  "Hooray!!, Your Charging Port Has been Booked";
        this.sendHtmlEmail(toEmail, subject, htmlContent);
    }

    public void sendCPClearingNotification(ChargingPortBooking cpbooking) throws Exception{
        String htmlContent =  String.format("""
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                </head>
                <body>
                <table >
                    <tr>
                        <td style = " align="center"; ">
                            <img    style="display: block; max-width: 100%%; height: auto; width: 100%%; border: 0;"
                                    src = "https://raw.githubusercontent.com/Pavithran-sde/skills-github/835b8d220ef04516c5892622bb42920cd619938c/CPClearingBooking.png" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h3>Dear %s,</h3>
                            <p>Thank you for using LibraryUserManagement Application. This Mail is to inform you about your Request for Clearing the Booking for Charging Port</p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <h4>Your Booking Details</h4>  
                        </td>
                    </tr>
                    <tr>
                       <td>
                        <table border="1px"  cellpadding="5px" cellspacing="5px" >
                            <tr>
                                <th>UserID</th>
                                <th>ChargingPortID</th>
                                <th>Floor</th>
                                <th>Wing</th>
                                <th>Reservation Start Time</th>
                                <th>Reservation End Time</th>
                                
                            </tr>
                            <tr style = " text-align :center">
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                        </table>
                        </td>
                    </tr>
                    <tr>
                        <h4>RESERVATION END TIME IS SUBJECT TO CHANGES BASED ON DEMAND,<br> YOU WILL BE NOTIFIED WHEN YOUR RESERVATION ENDS. <br> * YOU ARE GUARANTEED TO CHARGE FOR 1 HOUR MINIMUM * </h4>
                    </tr>
                </table>
                <h4>Status : %s</h4>
                </body>
                </html>
                """, cpbooking.getUser().getName(), cpbooking.getUser().getId(),
                cpbooking.getChargingPort().getId(), cpbooking.getChargingPort().getFloor().getFloorName(),
                cpbooking.getChargingPort().getFloor().getWing(), cpbooking.getReservationStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), cpbooking.getReservationEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), cpbooking.getStatus());

        String toEmail = cpbooking.getUser().getEmail();
        String subject =  "Woah!!,Cleared Your Charging Port Booking";
        this.sendHtmlEmail(toEmail, subject, htmlContent);
    }

        public void clearBookingNotificationForToday(User user) throws Exception{
        String htmlContent =  String.format("""
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                </head>
                <body>
                <table >
                    <tr>
                        <td style = " align="center"; ">
                            <img    style="display: block; max-width: 100%%; height: auto; width: 100%%; border: 0;"
                                    src = "https://raw.githubusercontent.com/Pavithran-sde/skills-github/refs/heads/main/REMINDER.png" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h3>Dear %s,</h3>
                            <p>Thank you for using Library User Management Application. This Mail is to inform you, The Library is about to close soon. So, kindly clear all your bookings while leaving.</p>
                        </td>
                    </tr>
                </table>
                </body>
                </html>
                """, user.getName());

        String toEmail = user.getEmail();
        String subject =  "#Reminder The Library closes Soon";
        this.sendHtmlEmail(toEmail, subject, htmlContent);
    }

    


}


