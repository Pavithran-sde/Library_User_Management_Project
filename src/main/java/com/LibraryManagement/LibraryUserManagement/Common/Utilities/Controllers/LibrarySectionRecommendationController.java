//package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Controllers;
//
//
//import com.LibraryManagement.LibraryUserManagement.Admin.Service.SectionService;
//import com.LibraryManagement.LibraryUserManagement.Admin.Service.SubjectService;
//import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class LibrarySectionRecommendationController {
//
//    @Autowired
//    private SubjectService subjectService;
//
//    @Autowired
//    private SectionService sectionService;
//
//    @GetMapping("/recommendBySubjectName/{subjectName}")
//    public ResponseEntity<?> getRecommendationForSubject(@PathVariable String subjectName){
//        try{
//            return new ResponseEntity<>(subjectService.getRecommendationBySubjectName(subjectName), HttpStatus.OK);
//        } catch( NotFoundException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Something Went Wrong, Please try again");
//        }
//    }
//
//    @GetMapping("/recommendBySectionName/{sectionName}")
//    public ResponseEntity<?> getRecommendationForSection(@PathVariable String sectionName){
//        try{
//            return new ResponseEntity<>(sectionService.getRecommendationBySectionName(sectionName), HttpStatus.OK);
//        } catch( NotFoundException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Something Went Wrong, Please try again");
//        }
//
//    }
//
//
//}
