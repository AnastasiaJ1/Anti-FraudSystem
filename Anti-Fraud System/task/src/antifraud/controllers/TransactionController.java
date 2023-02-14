package antifraud.controllers;

import antifraud.models.*;
import antifraud.models.dto.*;
import antifraud.models.responses.StatusResponse;
import antifraud.models.responses.TransactionResponse;
import antifraud.models.responses.UserDeleteResponse;
import antifraud.models.responses.UserResponse;
import antifraud.repositories.UserRepository;
import antifraud.services.impl.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class TransactionController {
    @Autowired
    DateHandlerImpl dateHandler;

    @Autowired
    RegionHandlerImpl regionHandler;
    @Autowired
    private TransactionHandlerImpl transactionHandler;

    @Autowired
    private IpServiceImpl ipService;

    @Autowired
    private CardServiceImpl cardService;

    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder encoder;




    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<?> putFeedback(@RequestBody FeedbackDTO feedbackDTO){
        HttpStatus status = transactionHandler.getFeedbackStatus(feedbackDTO);
        if(status.equals(HttpStatus.OK)){
            return new ResponseEntity<>(transactionHandler.putFeedback(feedbackDTO),HttpStatus.OK);
        }
        return new ResponseEntity<>(status);
    }

    @GetMapping("/api/antifraud/history")
    public ResponseEntity<?> getHistory(){
        return new ResponseEntity<>(transactionHandler.getHistory(), HttpStatus.OK);
    }

    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<?> getHistory(@PathVariable String number){
        if(!cardService.checkFormat(number)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpStatus status = transactionHandler.checkHistoryNumber(number);
        if(!status.equals(HttpStatus.OK)){
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(transactionHandler.getHistoryNumber(number), HttpStatus.OK);
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);
        if(!user.userIsValid()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if (!userRepo.save(user)) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.CREATED);
    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<?> userList() {
        return new ResponseEntity<>(userRepo.getListUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        UserDeleteResponse response = userRepo.deleteUser(username);
        if(response == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<?> putRole(@RequestBody RoleDTO role){
        HttpStatus status = userRepo.putRoleStatus(role);
        if(status.equals(HttpStatus.OK)){
            UserResponse user = userRepo.putRole(role);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        return new ResponseEntity<>(status);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<?> changeAccess(@RequestBody AccessDTO access){
        HttpStatus status = userRepo.changeAccessStatus(access);
        if(status.equals(HttpStatus.OK)){
            StatusResponse statusResponse = userRepo.changeAccess(access);
            return new ResponseEntity<>(statusResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>(status);
    }

    @PostMapping("api/antifraud/suspicious-ip")
    public ResponseEntity<?> addSuspiciousIp(@RequestBody SuspiciousIpDTO suspiciousIpDTO){
        HttpStatus status = ipService.addIpStatus(suspiciousIpDTO);
        if(!status.equals(HttpStatus.OK)) return new ResponseEntity<>(status);
        return new ResponseEntity<>(ipService.addIp(suspiciousIpDTO), status);
    }

    @GetMapping("api/antifraud/suspicious-ip")
    public ResponseEntity<?> getSuspiciousIpList(){
        return new ResponseEntity<>(ipService.getListIp(), HttpStatus.OK);
    }

    @DeleteMapping("api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteSuspiciousIp(@PathVariable String ip){
        HttpStatus status = ipService.deleteIpStatus(ip);
        if(!status.equals(HttpStatus.OK)) return new ResponseEntity<>(status);
        return new ResponseEntity<>(ipService.deleteIp(ip), status);
    }

    @PostMapping("api/antifraud/stolencard")
    public ResponseEntity<?> addCard(@RequestBody CardDTO cardDTO){
        HttpStatus status = cardService.addCardStatus(cardDTO);
        if(!status.equals(HttpStatus.OK)) return new ResponseEntity<>(status);
        return new ResponseEntity<>(cardService.addCard(cardDTO), status);
    }

    @GetMapping("api/antifraud/stolencard")
    public ResponseEntity<?> getCardList(){
        return new ResponseEntity<>(cardService.getListCard(), HttpStatus.OK);
    }

    @DeleteMapping("api/antifraud/stolencard/{number}")
    public ResponseEntity<?> deleteCardController(@PathVariable String number){
        HttpStatus status = cardService.deleteCardStatus(number);
        if(!status.equals(HttpStatus.OK)) return new ResponseEntity<>(status);
        return new ResponseEntity<>(cardService.deleteCard(number), status);
    }

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody @NonNull TransactionDTO request){
        String response = transactionHandler.handleTransaction(request);
        boolean statusIp = ipService.checkFormat(request.getIp());
        boolean statusCard = cardService.checkFormat(request.getNumber());
        Date date = dateHandler.checkDate(request.getDate());
        boolean region = regionHandler.checkRegion(request.getRegion());
        if(!statusIp || !statusCard || response == null || date == null || !region) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = new Transaction(request.getAmount(), request.getIp(),
                request.getNumber(), request.getRegion(), date);

        boolean isIpInBlackList = ipService.inBlackList(request.getIp());
        boolean isCardInBlackList = cardService.inBlackList(request.getNumber());
        transactionHandler.save(transaction);
        int diffRegions = transactionHandler.checkRegionRestriction(transaction);
        int diffIp = transactionHandler.checkIpRestriction(transaction);
        TransactionResponse response_tr = new TransactionResponse(response,
                isCardInBlackList, isIpInBlackList, diffRegions, diffIp);
        transaction.setResult(response_tr.getResult());
        transactionHandler.save(transaction);
        return new ResponseEntity<>(response_tr, HttpStatus.OK);
    }

}
