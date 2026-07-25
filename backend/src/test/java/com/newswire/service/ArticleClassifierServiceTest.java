package com.newswire.service;

import com.newswire.article.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleClassifierServiceTest {

    private ArticleClassifierService classifierService;

    @BeforeEach
    void setUp() {
        classifierService = new ArticleClassifierService();
    }


    //------------------------------------------------------
    // Technology Tests
    //------------------------------------------------------
    @Test
    void shouldClassifyArtificialIntelligenceAsTechnology() {
        Category result = classifierService.classifyCategory(
            "Artificial intelligence is transforming software development",
            "Companies are investing heavily in machine learning tools.",
            Category.GENERAL
        );

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldClassifyCybersecurityAsTechnology() {
        Category result = classifierService.classifyCategory(
            "Cybersecurity teams respond to major data breach",
            "The breach affected cloud computing systems.",
            Category.GENERAL
        );

        assertEquals(Category.TECHNOLOGY, result);
    }

    //------------------------------------------------------
    // Politics Tests
    //------------------------------------------------------
    @Test
    void shouldClassifyPoliticalArticleAsPolitics() {
        Category result = classifierService.classifyCategory(
            "Congress debates new legislation",
            "The White House is working with the Senate.",
            Category.GENERAL
        );

        assertEquals(Category.POLITICS, result);
    }

    @Test
    void shouldClassifyElectionArticleAsPolitics() {
        Category result = classifierService.classifyCategory(
            "Presidential election campaign enters final week",
            "The government is preparing for election day.",
            Category.GENERAL
        );

        assertEquals(Category.POLITICS, result);
    }

    //----------------------------------------------------------
    // Fall Behavior Tests
    //----------------------------------------------------------
    @Test
    void shouldReturnFallbackWhenNoCategoryReachesMinimumScore() {
        Category result = classifierService.classifyCategory(
            "Local community holds summer festival",
            "Residents attended events throughout the weekend.",
            Category.GENERAL
        );

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldPreserveConfiguredFallbackCategory() {
        Category result = classifierService.classifyCategory(
            "Local business opens new office",
            "The company welcomed employees to the building.",
            Category.FINANCE
        );

        assertEquals(Category.FINANCE, result);
    }

    @Test
    void shouldReturnGeneralWhenFallbackIsNull() {
        Category result = classifierService.classifyCategory(
            "Local community holds summer festival",
            "Residents attended events throughout the weekend.",
            null
        );

        assertEquals(Category.GENERAL, result);
    }

    //-------------------------------------------------------
    // Handle Null Title Tests
    //-------------------------------------------------------
    @Test
    void shouldHandleNullTitle() {
        Category result = classifierService.classifyCategory(
            null,
            "Artificial intelligence and machine learning software",
            Category.GENERAL
        );

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldHandleNullSummary() {
        Category result = classifierService.classifyCategory(
            "Congress approves new legislation",
            null,
            Category.GENERAL
        );

        assertEquals(Category.POLITICS, result);
    }

    @Test
    void shouldHandleNullTitleAndSummary() {
        Category result = classifierService.classifyCategory(
            null,
            null,
            Category.GENERAL
        );

        assertEquals(Category.GENERAL, result);
    }

    //------------------------------------------------------
    // Ignore Case Tests
    //------------------------------------------------------
    @Test
    void shouldIgnoreCaseWhenClassifying() {
        Category result = classifierService.classifyCategory(
            "ARTIFICIAL INTELLIGENCE AND MACHINE LEARNING",
            "SOFTWARE TECHNOLOGY",
            Category.GENERAL
        );

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldIgnorePunctuationWhenClassifying() {
        Category result = classifierService.classifyCategory(
            "Artificial-intelligence, machine-learning, and software!",
            "Technology continues to advance.",
            Category.GENERAL
        );

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldNotMatchAiInsideSaid() {
        Category result = classifierService.classifyCategory(
            "Officials said the event was successful",
            "Residents said attendance was high.",
            Category.GENERAL
        );

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldReturnFallbackWhenTechnologyAndPoliticsTie() {
        Category result = classifierService.classifyCategory(
            "Congress discusses cybersecurity",
            "",
            Category.GENERAL
        );

        assertEquals(Category.GENERAL, result);
    }

    //-------------------------------------------------------
    // Finance Tests
    //-------------------------------------------------------
    @Test
    void shouldClassifyCreditRatingAsFinance() {
        Category result = classifierService.classifyCategory(
            "Moody's lowers the nation's credit rating",
            "Investors reacted to the downgrade.",
            Category.GENERAL
        );

        assertEquals(Category.FINANCE, result);
    }

    @Test
    void shouldNotClassifyCreditRateAsFinance() {
        Category result = classifierService.classifyCategory(
            "Bank announces lower credit rate for customers",
            "The offer begins next month.",
            Category.GENERAL
        );

        assertEquals(Category.GENERAL, result);
    }

    //-------------------------------------------------------
    // Healthcare Tests
    //-------------------------------------------------------
    @Test
    void shouldClassifyCdcAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "CDC issues new health advisory",
            "The Centers for Disease Control is monitoring the outbreak.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldClassifyFdaAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "FDA approves new cancer drug",
            "The Food and Drug Administration announced the approval.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldClassifyClinicalTrialAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "Clinical trial shows promising cancer treatment",
            "Medical research continues at major hospitals.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldClassifyMedicalResearchAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "Medical research discovers new treatment",
            "Researchers published results from a nationwide study.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldClassifyHospitalArticleAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "Local hospital expands emergency department",
            "Physicians expect improved patient care.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldClassifyVaccineArticleAsHealthcare() {
        Category result = classifierService.classifyCategory(
            "New vaccine enters nationwide distribution",
            "Public health officials encourage vaccination.",
            Category.GENERAL
        );

        assertEquals(Category.HEALTHCARE, result);
    }

    // ---------------------------------------------------------------------
    // Military Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyCarrierStrikeGroupAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Carrier Strike Group Deployed",
                "The carrier strike group began operations in the Pacific.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyBallisticMissileAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Ballistic Missile Test",
                "The military successfully launched a ballistic missile.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyUavAsMilitary() {
        Category result = classifierService.classifyCategory(
                "UAV Conducts Reconnaissance",
                "An unmanned aerial vehicle completed its mission.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyAirForceAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Air Force Announces New Squadron",
                "The Air Force activated another squadron this week.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyTroopsAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Troops Arrive Overseas",
                "Thousands of troops arrived for a joint exercise.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifySubmarineAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Attack Submarine Returns",
                "The submarine completed its patrol successfully.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifySpecialOperationsAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Special Operations Mission",
                "Special operations forces conducted a nighttime raid.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyJointTaskForceAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Joint Task Force Activated",
                "A joint task force was formed for disaster response.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldClassifyNuclearWeaponsAsMilitary() {
        Category result = classifierService.classifyCategory(
                "Nuclear Weapons Treaty",
                "Officials discussed nuclear weapons policy.",
                Category.GENERAL);

        assertEquals(Category.MILITARY, result);
    }

    @Test
    void shouldUseFallbackForMilitaryBaseOnly() {
        Category result = classifierService.classifyCategory(
                "Military Base Renovation",
                "The military base received new housing.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    } 

    // ---------------------------------------------------------------------
    // Military Intelligence Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyForeignIntelligenceServiceAsMilitaryIntelligence() {
        Category result = classifierService.classifyCategory(
                "Foreign Intelligence Service Expands Operations",
                "The foreign intelligence service increased overseas collection activity.",
                Category.GENERAL);

        assertEquals(Category.MILITARY_INTELLIGENCE, result);
    }

    @Test
    void shouldClassifySignalsIntelligenceAsMilitaryIntelligence() {
        Category result = classifierService.classifyCategory(
                "Signals Intelligence Supports Mission",
                "Signals intelligence analysts reviewed intercepted communications.",
                Category.GENERAL);

        assertEquals(Category.MILITARY_INTELLIGENCE, result);
    }

    @Test
    void shouldClassifyCyberEspionageAsMilitaryIntelligence() {
        Category result = classifierService.classifyCategory(
                "Cyber Espionage Campaign Discovered",
                "Investigators uncovered a cyber espionage operation targeting defense networks.",
                Category.GENERAL);

        assertEquals(Category.MILITARY_INTELLIGENCE, result);
    }

    @Test
    void shouldClassifyNationalSecurityAgencyAsMilitaryIntelligence() {
        Category result = classifierService.classifyCategory(
                "National Security Agency Releases Report",
                "The National Security Agency published a new intelligence assessment.",
                Category.GENERAL);

        assertEquals(Category.MILITARY_INTELLIGENCE, result);
    }

    @Test
    void shouldClassifySatelliteReconnaissanceAsMilitaryIntelligence() {
        Category result = classifierService.classifyCategory(
                "Satellite Reconnaissance Detects Activity",
                "Satellite reconnaissance identified unusual movement near the border.",
                Category.GENERAL);

        assertEquals(Category.MILITARY_INTELLIGENCE, result);
    }

    // ---------------------------------------------------------------------
    // Military Intelligence Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldFallbackToGeneralForForeignService() {
        Category result = classifierService.classifyCategory(
                "Foreign Service Officers Meet",
                "Foreign service officers discussed diplomatic assignments.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldFallbackToGeneralForReconOnly() {
        Category result = classifierService.classifyCategory(
                "Recon Team Prepares Equipment",
                "The recon team reviewed its equipment before departure.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    // ---------------------------------------------------------------------
    // World Population Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyPopulationGrowthAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Global Population Growth Continues",
                "Researchers project continued population growth across several regions.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyFertilityRateAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Fertility Rate Reaches Record Low",
                "The country's fertility rate declined for another consecutive year.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyCensusDataAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "New Census Data Released",
                "The census data shows major demographic changes across the country.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyAgingPopulationAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Aging Population Reshapes Society",
                "An aging population is increasing the nation's median age.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyMigrationTrendsAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Migration Trends Shift Population",
                "New migration trends are changing regional population patterns.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyLifeExpectancyAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Life Expectancy Increases",
                "Life expectancy increased as mortality rates continued to decline.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    @Test
    void shouldClassifyUrbanizationAsWorldPopulation() {
        Category result = classifierService.classifyCategory(
                "Urbanization Accelerates Worldwide",
                "Urbanization is increasing the size of the global urban population.",
                Category.GENERAL);

        assertEquals(Category.WORLD_POPULATION, result);
    }

    // ---------------------------------------------------------------------
    // World Population Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldUseFallbackForPopulationOnly() {
        Category result = classifierService.classifyCategory(
                "Local Population Discusses New Park",
                "The population attended a meeting about a neighborhood park.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldUseFallbackForResidentOnly() {
        Category result = classifierService.classifyCategory(
                "Resident Receives Community Award",
                "A resident was recognized for volunteer service.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    // ---------------------------------------------------------------------
    // World Economies Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyGrossDomesticProductAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Gross Domestic Product Expands",
                "The country's gross domestic product increased during the latest quarter.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyEconomicGrowthAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Global Economic Growth Slows",
                "Economic growth weakened across several major economies.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyInflationAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Inflation Remains Elevated",
                "Consumer price index data showed inflation remained above expectations.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyMonetaryPolicyAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Central Bank Changes Monetary Policy",
                "The central bank adjusted monetary policy and its benchmark interest rate.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyInternationalTradeAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "International Trade Reaches New High",
                "Exports and imports increased as global trade continued to expand.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyTradeDeficitAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Trade Deficit Widens",
                "The trade deficit increased as imports grew faster than exports.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyInternationalMonetaryFundAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "International Monetary Fund Releases Outlook",
                "The International Monetary Fund lowered its economic growth forecast.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifyExchangeRateAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Exchange Rate Falls",
                "The exchange rate declined after the central bank announced new policy.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    @Test
    void shouldClassifySovereignDebtAsWorldEconomies() {
        Category result = classifierService.classifyCategory(
                "Sovereign Debt Pressures Increase",
                "Rising sovereign debt is affecting the country's economic outlook.",
                Category.GENERAL);

        assertEquals(Category.WORLD_ECONOMIES, result);
    }

    // ---------------------------------------------------------------------
    // World Economies Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldUseFallbackForEconomicOnly() {
        Category result = classifierService.classifyCategory(
                "Economic",
                "",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldUseFallbackForCurrencyOnly() {
        Category result = classifierService.classifyCategory(
                "Currency",
                "",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    // ---------------------------------------------------------------------
    // Sports Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyFootballAsSports() {
        Category result = classifierService.classifyCategory(
                "Premier League Club Signs New Striker",
                "The football club completed the transfer before the new season.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyWorldCupAsSports() {
        Category result = classifierService.classifyCategory(
                "World Cup Final Draws Record Audience",
                "The championship match ended after a dramatic penalty shootout.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyBasketballAsSports() {
        Category result = classifierService.classifyCategory(
                "NBA Team Wins Playoff Game",
                "The basketball team advanced after a strong fourth quarter.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyAmericanFootballAsSports() {
        Category result = classifierService.classifyCategory(
                "Quarterback Leads Team to Super Bowl",
                "The NFL quarterback threw three touchdowns in the championship game.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyBaseballAsSports() {
        Category result = classifierService.classifyCategory(
                "Pitcher Throws Complete Game",
                "The MLB pitcher allowed only one hit across nine innings.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyFormulaOneAsSports() {
        Category result = classifierService.classifyCategory(
                "Norris Takes Pole Position",
                "The Formula One driver will start the Grand Prix from the front row.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyCyclingAsSports() {
        Category result = classifierService.classifyCategory(
                "Pogacar Wins Tour de France Stage",
                "The cyclist attacked the peloton during the final mountain climb.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyTennisAsSports() {
        Category result = classifierService.classifyCategory(
                "Wimbledon Champion Saves Match Point",
                "The tennis player won the Grand Slam final in five sets.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyGolfAsSports() {
        Category result = classifierService.classifyCategory(
                "McIlroy Wins PGA Tournament",
                "The golfer made a birdie on the final hole to claim the championship.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyBoxingAsSports() {
        Category result = classifierService.classifyCategory(
                "Heavyweight Champion Wins by Knockout",
                "The boxing match ended in the seventh round.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyOlympicsAsSports() {
        Category result = classifierService.classifyCategory(
                "Olympic Athlete Wins Gold Medal",
                "The athlete set a championship record at the Olympics.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    @Test
    void shouldClassifyNamedAthleteAsSports() {
        Category result = classifierService.classifyCategory(
                "Hamilton Cautious Ahead of Hungary",
                "Lewis Hamilton says the next Grand Prix will test the team.",
                Category.GENERAL);

        assertEquals(Category.SPORTS, result);
    }

    // ---------------------------------------------------------------------
    // Sports Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldUseFallbackForTeamOnly() {
        Category result = classifierService.classifyCategory(
                "Project Team Holds Weekly Meeting",
                "The team reviewed its work and upcoming deadlines.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldUseFallbackForManagerOnly() {
        Category result = classifierService.classifyCategory(
                "Manager Announces Office Changes",
                "The manager discussed a revised workplace schedule.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldUseFallbackForSeasonOnly() {
        Category result = classifierService.classifyCategory(
                "Holiday Season Begins",
                "Local stores prepared for the busy shopping season.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    // ---------------------------------------------------------------------
    // Entertainment Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyMovieAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "New Movie Breaks Box Office Records",
                "The film earned more than expected during its opening weekend.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyStreamingSeriesAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Netflix Announces New Television Series",
                "The streaming platform released the trailer for its upcoming drama.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyMusicAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Singer Releases New Album",
                "The musician will begin a concert tour later this year.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyEntertainmentAwardsAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Academy Awards Nominees Announced",
                "Several actors and directors received Oscar nominations.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyCelebrityAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Taylor Swift Announces New Tour",
                "The singer will perform concerts across several countries.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyVideoGamesAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Nintendo Reveals New Video Game",
                "The gaming company announced the title during a livestream.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    @Test
    void shouldClassifyBroadwayAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Broadway Musical Opens This Week",
                "The production features a new cast and original music.",
                Category.GENERAL);

        assertEquals(Category.ENTERTAINMENT, result);
    }

    // ---------------------------------------------------------------------
    // Entertainment Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldNotClassifyBusinessTourAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Factory Manager Begins Regional Tour",
                "The manager will visit several production facilities.",
                Category.GENERAL);

        assertEquals(Category.GENERAL, result);
    }

    @Test
    void shouldNotClassifySoftwareStreamingAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Company Improves Real-Time Data Streaming",
                "The technology platform processes live sensor data.",
                Category.GENERAL);

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldNotClassifyPoliticalDramaAsEntertainment() {
        Category result = classifierService.classifyCategory(
                "Political Drama Continues in Congress",
                "Lawmakers debated the bill during a tense Senate hearing.",
                Category.GENERAL);

        assertEquals(Category.POLITICS, result);
    }

    // ---------------------------------------------------------------------
    // Culture Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldClassifyMuseumAsCulture() {
        Category result = classifierService.classifyCategory(
                "Museum Opens New Art Exhibition",
                "The gallery features paintings and sculptures from local artists.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyLiteratureAsCulture() {
        Category result = classifierService.classifyCategory(
                "Award-Winning Author Publishes New Novel",
                "The book explores history, identity, and family tradition.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyArchaeologyAsCulture() {
        Category result = classifierService.classifyCategory(
                "Archaeologists Discover Ancient Artifacts",
                "The excavation uncovered ruins from a previously unknown civilization.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyReligionAsCulture() {
        Category result = classifierService.classifyCategory(
                "Vatican Announces New Pilgrimage",
                "Church leaders described the religious ceremony and its historic importance.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyLanguageAsCulture() {
        Category result = classifierService.classifyCategory(
                "Researchers Document Endangered Dialect",
                "The linguistics project preserves the language through translation and recordings.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyHeritageAsCulture() {
        Category result = classifierService.classifyCategory(
                "UNESCO Adds Site to World Heritage List",
                "The historic location is known for its indigenous traditions and architecture.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    @Test
    void shouldClassifyFolkloreAsCulture() {
        Category result = classifierService.classifyCategory(
                "Festival Celebrates Local Folklore",
                "The ceremony includes traditional music, customs, and storytelling.",
                Category.GENERAL);

        assertEquals(Category.CULTURE, result);
    }

    // ---------------------------------------------------------------------
    // Culture Fallback Tests
    // ---------------------------------------------------------------------

    @Test
    void shouldNotClassifySoftwareArchitectureAsCulture() {
        Category result = classifierService.classifyCategory(
                "Company Redesigns Cloud Architecture",
                "Engineers improved the software platform and database structure.",
                Category.GENERAL);

        assertEquals(Category.TECHNOLOGY, result);
    }

    @Test
    void shouldNotClassifyMedicalHistoryAsCulture() {
        Category result = classifierService.classifyCategory(
                "Doctors Review Patient Medical History",
                "The physician evaluated the diagnosis and prior treatment.",
                Category.GENERAL);

        assertEquals(Category.HEALTHCARE, result);
    }

    @Test
    void shouldNotClassifyFinancialBookAsCultureFromBookAlone() {
        Category result = classifierService.classifyCategory(
                "Company Updates Its Accounting Book",
                "The finance department reviewed expenses and revenue records.",
                Category.GENERAL);

        assertEquals(Category.FINANCE, result);
    }


}