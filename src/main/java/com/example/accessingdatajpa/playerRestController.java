package com.example.accessingdatajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.management.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
class playerRestController {

    private final PlayerRepository playerRepository;
    private final PlayerPerformanceRepository perfRepository;

    private final TeamRepository teamRepository;


    playerRestController(PlayerRepository playerRepository, PlayerPerformanceRepository perfRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.perfRepository = perfRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/Performance")
    public String allPerfs(@RequestParam(name = "points", required = false, defaultValue = "0") int Points, @RequestParam(name = "rebounds", required = false, defaultValue = "0") int Rebounds,
                           @RequestParam(name = "assists", required = false, defaultValue = "0") int Assists, @RequestParam(name = "steals", required = false, defaultValue = "0") int Steals,
                           @RequestParam(name = "blocks", required = false, defaultValue = "0") int Blocks, @RequestParam(name = "mins", required = false, defaultValue = "0") int Minutes,
                           @RequestParam(name = "fname", required = false, defaultValue = "-") String firstName, @RequestParam(name = "lname", required = false, defaultValue = "-") String lastName,
                           @RequestParam(name = "num", required = false, defaultValue = "-1") int Number, @RequestParam(name = "pos", required = false, defaultValue = "0") int Position,
                           @RequestParam(name = "tname", required = false, defaultValue = "0") String tName, @RequestParam(name = "eid", required = false, defaultValue = "-1") Long editID,
                           @RequestParam(name = "pid", required = false, defaultValue = "0") Long pID, Model model) throws Exception {
        if (editID > -1) {
            if (pID < 0) {
                PlayerPerformance p = perfRepository.findById(editID).get();
                Player player = playerRepository.findById(p.getPlayerID()).get();
                player.setGp(player.getGp() - 1);
                player.setPpg(player.getPpg() - p.getPoints());
                player.setRpg(player.getRpg() - p.getRebounds());
                player.setApg(player.getApg() - p.getAssists());
                player.setMpg(player.getMpg() - p.getMinutes());
                player.setBpg(player.getBpg() - p.getBlocks());
                player.setSpg(player.getSpg() - p.getSteals());
                player.setSpg(player.getSt() - p.getScore());


                perfRepository.deleteById(editID);

                if (player.getGp() == 0) {

                    Team aa = teamRepository.findByName(player.getTeamName()).iterator().next();
                    aa.removePlayer();
                    if (aa.getTeamCaptain() == player.getNumber()) {
                        List<Player> curr = playerRepository.findByTeamName(player.getTeamName());
                        if(curr.size() > 1) {
                            int i = 0;
                            while(curr.get(i).getNumber() == player.getNumber()) {
                                i++;
                            }
                            aa.setTeamCaptain(curr.get(i).getNumber());
                            teamRepository.save(aa);
                        } else {
                            teamRepository.deleteById(aa.getId());
                        }
                    } else {
                        teamRepository.save(aa);
                    }
                    playerRepository.deleteById(player.getId());


                } else {

                    playerRepository.save(player);
                }

            } else {
                PlayerPerformance p = perfRepository.findById(editID).get();
                Player player = playerRepository.findById(p.getPlayerID()).get();
                player.setGp(player.getGp() - 1);
                player.setPpg(player.getPpg() - p.getPoints());
                player.setRpg(player.getRpg() - p.getRebounds());
                player.setApg(player.getApg() - p.getAssists());
                player.setMpg(player.getMpg() - p.getMinutes());
                player.setBpg(player.getBpg() - p.getBlocks());
                player.setSpg(player.getSpg() - p.getSteals());
                player.setSt(player.getSt() - p.getScore());
                Player ogPlayer = player;

                p.setPlayerID(pID);
                p.setAssists(Assists);
                p.setPoints(Points);
                p.setBlocks(Blocks);
                p.setRebounds(Rebounds);
                p.setSteals(Steals);
                p.setMinutes(Minutes);
                p.setScore(p.calculateScore());

                player = playerRepository.findById(pID).get();
                player.setGp(player.getGp() + 1);
                player.setPpg(player.getPpg() + p.getPoints());
                player.setRpg(player.getRpg() + p.getRebounds());
                player.setApg(player.getApg() + p.getAssists());
                player.setMpg(player.getMpg() + p.getMinutes());
                player.setBpg(player.getBpg() + p.getBlocks());
                player.setSpg(player.getSpg() + p.getSteals());
                player.setSt(player.getSt() + p.getScore());

                if (ogPlayer.getGp() == 0) {
                    Team aa = teamRepository.findByName(ogPlayer.getTeamName()).iterator().next();
                    aa.removePlayer();
                    if (aa.getTeamCaptain() == ogPlayer.getNumber()) {
                        List<Player> curr = playerRepository.findByTeamName(ogPlayer.getTeamName());
                        if(curr.size() > 1) {
                            int i = 0;
                            while(curr.get(i).getNumber() == ogPlayer.getNumber()) {
                                i++;
                            }
                            aa.setTeamCaptain(curr.get(i).getNumber());
                            teamRepository.save(aa);
                        } else {
                            teamRepository.deleteById(aa.getId());
                        }
                    } else {
                        teamRepository.save(aa);
                    }
                    playerRepository.deleteById(ogPlayer.getId());

                }
                perfRepository.save(p);
            }

        } else {
            if (Number != -1) {
                if (firstName.equals("-")) {
                    perfRepository.save(new PlayerPerformance(Points, Assists,
                            Rebounds, Steals, Blocks, Minutes,
                            Number, tName, this.playerRepository));
                } else {
                    perfRepository.save(new PlayerPerformance(Points, Assists,
                            Rebounds, Steals, Blocks, Minutes, firstName,
                            lastName, Number, Position, tName, this.playerRepository, this.teamRepository));
                }
            }
        }

        List<PlayerPerformance> repo = new ArrayList<PlayerPerformance>();
        Iterator<PlayerPerformance> it = perfRepository.findAll().iterator();
        while (it.hasNext()) {
            repo.add(it.next());
        }
        model.addAttribute("repo", repo);
        return "Performances";
    }

    @GetMapping("/Player")
    public String viewPlayer(Model model) {
        List<Player> repo = new ArrayList<Player>();
        Iterator<Player> it = playerRepository.findAll().iterator();
        while (it.hasNext()) {
            repo.add(it.next());
        }
        model.addAttribute("repo", repo);
        return "Player";
    }

    @GetMapping("/report")
    public String repScreen(Model model) {
        FormData formData = new FormData();
        model.addAttribute("formData", formData);
        return "createReport";
    }
    @GetMapping("/editPerformance")
    public String editPerf(@RequestParam(name = "editID", required = true) int editID, Model model) {
        FormData formData = new FormData();
        model.addAttribute("formData", formData);
        model.addAttribute("editId", editID);

        return "editPerformance";
    }

    @GetMapping("/addPerformance")
    public String createPerf(Model model) {
        FormData formData = new FormData();
        model.addAttribute("formData", formData);
        return "addPerformances";
    }
    @GetMapping("/Team")
    public String returnTeam(Model model) {
        List<Team> repo = new ArrayList<>();
        Iterator<Team> curr = teamRepository.findAll().iterator();
        curr.forEachRemaining((team) -> repo.add(team));
        model.addAttribute("repo", repo);
        return "Teams";
    }

    @GetMapping("/madePlayReport")
    public String queryPlayerRep(@RequestParam(name = "Field") String Field,
                               @RequestParam(name = "Order") String Order,
                               @RequestParam(name = "Limit") String Limit,
                               @RequestParam(name="Bounds") String Bound,
                               @RequestParam(name="Comparator") String Comparator, Model model) throws SQLException {
        String ogField;
        if (Field.toLowerCase().equals("0")) {
            ogField = "Points";
            Field = "ppg";
        } else if (Field.toLowerCase().equals("1")) {
            ogField = "Score";
            Field = "st";
        } else if (Field.toLowerCase().equals("2")) {
            ogField = "Rebounds";
            Field = "rpg";
        } else if (Field.toLowerCase().equals("3")) {
            ogField = "Assists";
            Field = "apg";
        } else if (Field.toLowerCase().equals("4")) {
            ogField = "Blocks";
            Field = "bpg";
        } else {
            ogField = "Steals";
            Field = "spg";
        }
        String QUERY;
        if (Comparator.equals("<=")) {
            if (Order.equals("DESC")) {
                QUERY = "SELECT id as `id`, first_name as `firstName`, last_name as `lastName`, " + Field + " as `reqStat`, " + Field +
                        " / player.gp as `spm`, gp as `m` FROM player WHERE " + Field + " <= ? ORDER BY spm DESC LIMIT ? ;";
            } else {
                QUERY = "SELECT id as `id`, first_name as `firstName`, last_name as `lastName`, " + Field + " as `reqStat`, " + Field +
                        " / player.gp as `spm`, gp as `m` FROM player WHERE " + Field + " <= ? ORDER BY spm ASC LIMIT ? ;";
            }

        } else {
            if (Order.equals("DESC")) {
                QUERY = "SELECT id as `id`, first_name as `firstName`, last_name as `lastName`, " + Field + " as `reqStat`, " + Field +
                        " / player.gp as `spm`, gp as `m` FROM player WHERE " + Field + " >= ? ORDER BY spm DESC LIMIT ? ;";
            } else {
                QUERY = "SELECT id as `id`, first_name as `firstName`, last_name as `lastName`, " + Field + " as `reqStat`, " + Field +
                        " / player.gp as `spm`, gp as `m` FROM player WHERE " + Field + " >= ? ORDER BY spm ASC LIMIT ? ;";
            }
        }
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement(QUERY);
        ps.setInt(1, Integer.parseInt(Bound));
        ps.setInt(2, Integer.parseInt(Limit));
        ResultSet r = ps.executeQuery();
        ArrayList<String[]> ret = new ArrayList<>();
        while (r.next()) {
            ret.add(new String[]{r.getString("id"), r.getString("firstName"), r.getString("lastName"), r.getString("reqStat"), r.getString("spm"), r.getString("m")});
        }
        model.addAttribute("repo", ret);
        model.addAttribute("stat", ogField);
        model.addAttribute("type", "Generated Player Report:");
        model.addAttribute("spm", ogField + " per Game");
        model.addAttribute("m", "Games Played");

        return "madeReport";

    }

    @GetMapping("/madePerfReport")
    public String queryPerfRep(@RequestParam(name = "Field") String Field,
                             @RequestParam(name = "Order") String Order,
                             @RequestParam(name = "Limit") String Limit,
                               @RequestParam(name="Bounds") String Bound,
                                @RequestParam(name="Comparator") String Comparator, Model model) throws SQLException {
        if (Field.toLowerCase().equals("0")) {
            Field = "points";
        } else if (Field.toLowerCase().equals("1")) {
            Field = "score";
        } else if (Field.toLowerCase().equals("2")) {
            Field = "rebounds";
        } else if (Field.toLowerCase().equals("3")) {
            Field = "assists";
        } else if (Field.toLowerCase().equals("4")) {
            Field = "blocks";
        } else {
            Field = "steals";
        }
        String QUERY;
        if (Comparator.equals(("<="))) {
            if (Order.equals("DESC")) {
                 QUERY = "SELECT player_performance.id as `id`, player.first_name as `firstName`, player.last_name as `lastName`, player_performance."
                        + Field + " as `reqStat`, " + Field + " / player_performance.minutes as `spm`, player_performance.minutes as `m` FROM player_performance INNER JOIN player ON player_performance.playerid=player.id WHERE player_performance."
                        + Field + " <= ? ORDER BY spm DESC LIMIT ?;";
            } else {
                 QUERY = "SELECT player_performance.id as `id`, player.first_name as `firstName`, player.last_name as `lastName`, player_performance."
                        + Field + " as `reqStat`, " + Field + " / player_performance.minutes as `spm`, player_performance.minutes as `m` FROM player_performance INNER JOIN player ON player_performance.playerid=player.id WHERE player_performance."
                        + Field + " <= ? ORDER BY spm ASC LIMIT ?;";
            }
        } else {
            if (Order.equals("DESC")) {
                QUERY = "SELECT player_performance.id as `id`, player.first_name as `firstName`, player.last_name as `lastName`, player_performance."
                        + Field + " as `reqStat`, " + Field + " / player_performance.minutes as `spm`, player_performance.minutes as `m` FROM player_performance INNER JOIN player ON player_performance.playerid=player.id WHERE player_performance."
                        + Field + " >= ? ORDER BY spm DESC LIMIT ?;";
            } else {
                QUERY = "SELECT player_performance.id as `id`, player.first_name as `firstName`, player.last_name as `lastName`, player_performance."
                        + Field + " as `reqStat`, player_performance." + Field + " / player_performance.minutes as `spm`, player_performance.minutes as `m` FROM player_performance INNER JOIN player ON player_performance.playerid=player.id WHERE player_performance."
                        + Field + " >= ? ORDER BY spm ASC LIMIT ?;";
            }
        }
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement(QUERY);
        ps.setInt(1, Integer.parseInt(Bound));
        ps.setInt(2, Integer.parseInt(Limit));
        ResultSet r = ps.executeQuery();
        ArrayList<String[]> ret = new ArrayList<>();
        while (r.next()) {
            ret.add(new String[]{r.getString("id"), r.getString("firstName"), r.getString("lastName"), r.getString("reqStat"), r.getString("spm"),r.getString("m")});
        }
        model.addAttribute("repo", ret);
        model.addAttribute("stat", Field);
        model.addAttribute("type", "Generated Performance Report:");
        model.addAttribute("spm", Field + " per Minute");
        model.addAttribute("m", "Minutes Played");

        return "madeReport";

    }

    @Autowired
    JdbcTemplate jdbcTemplate;


}

class FormData {
    private String input1 = "";
    private String input2;
    private String input3;
    private String input4;
    private String input5;
    private String input6;
    private String input7;
    private String input8;
    private String input9;
    private String input10;
    private String input11;
    private String input12;

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getInput4() {
        return input4;
    }

    public void setInput4(String input4) {
        this.input4 = input4;
    }

    public String getInput5() {
        return input5;
    }

    public void setInput5(String input5) {
        this.input5 = input5;
    }

    public String getInput6() {
        return input6;
    }

    public void setInput6(String input6) {
        this.input6 = input6;
    }

    public String getInput7() {
        return input7;
    }

    public void setInput7(String input7) {
        this.input7 = input7;
    }

    public String getInput8() {
        return input8;
    }

    public void setInput8(String input8) {
        this.input8 = input8;
    }

    public String getInput9() {
        return input9;
    }

    public void setInput9(String input9) {
        this.input9 = input9;
    }

    public String getInput10() {
        return input10;
    }

    public void setInput10(String input10) {
        this.input10 = input10;
    }

    public String getInput11() {
        return input11;
    }

    public void setInput11(String input11) {
        this.input11 = input11;
    }

    public String getInput12() {
        return input12;
    }

    public void setInput12(String input12) {
        this.input12 = input12;
    }

    public FormData() {
        // Default constructor
    }
}