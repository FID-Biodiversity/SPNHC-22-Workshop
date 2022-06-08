<!DOCTYPE html>
<html lang="de" prefix="og:http://ogp.me/ns#">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700;900&display=swap"
          rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script src="https://kit.fontawesome.com/a01f8fee95.js" crossorigin="anonymous"></script>

    <script src="js/simpleXML.js"></script>
    <link rel="stylesheet" href="css/simpleXML.css"/>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>

    <title>SPNHC 22 Workshop #TTLab #BioFID</title>

    <style type="text/css">

        * {
            font-family: 'Noto Sans TC', sans-serif;
            font-size: 1vw;
        }

        body {
            background-color: #4c7828;
            padding: 0pt;
            margin: 0pt;
        }

        h1, h2, h3, h4, h5, h6 {
            color: #000;
        }


        h1 {
            font-size: 5vw !important;
            text-align: center;
        }

        h2 {
            font-size: 4vw !important;
        }

        h3 {
            font-size: 3vw !important;
            text-align: center;
            padding: 0pt;
            margin: 0pt;
            margin-top: auto;
            margin-bottom: auto;
            width: 100%;
            color: #4c7828;
            display: inline;

        }

        h4 {
            font-size: 2vw !important;
        }

        p, label, li {
            color: #444 !important;
        }

        a {
            color: #0098D3 !important;
        }

        .ttl-header {
            /*background: linear-gradient(to bottom, #005eaa 0%, #337ebb 100%);*/
            background: #fff;
            color: #4c7828;
            padding: 1em 1em 0em 1em;
            margin-top: 0pt;
            padding-top: 0pt;
            display: grid;
            grid-template-columns: fit-content(10%) auto fit-content(10%);
            /*1fr 2fr 1fr;*/
            grid-column-gap: 5vmin;
            margin-left: auto;
            margin-right: auto;
            width: 90%; /* The width is 100%, when the viewport is 800px or smaller */
            max-height: 150px;
        }

        .element {

            padding:5pt;

        }

        .content {
            position: relative;
            margin-left: auto;
            margin-right: auto;
            background: white;
            width: 90%;
            bottom: 0;
            top: 0;
            padding: 1em;
            margin-top: 0.3em;

        }

        .headline{
            background-color: #4c7828;
            color:#fff;
            font-weight:bold;
            width:100%;
            display: inline-block;
            border: #fff 2pt solid;
            padding:4pt;
        }

        .logo {
            /*padding:1em 1em 1em 0;*/
            height: 18vh;
            margin-top: auto;
            position: relative;
            margin-bottom: auto;
        }



        div.button {
            border: #ccc solid 3pt;
            text-align: center;
            padding: 5pt;
            font-size: 3vw;
        }

        div.button i {
            font-size: 3vw;
        }

        div.button:hover {
            border: #1DA1F2 solid 3pt;
            padding: 5pt;
        }

        div.annotation {
            margin-top: 1em;
            padding-top: 0em;
        }

        label {
            text-align: center;
            font-size: 1vw;
            font-weight: bold;
        }

        #xmlviewer .simpleXML {
            font-size: 12pt;
        }

        .simpleXML {
            font-size: 12pt !important;
        }

    </style>

</head>


<body>
<div class="ttl-header">
    <div></div>
    <h3 style="align:center;">SPNHC 22 Workshop</h3>
    <div></div>
</div>

<div class="content">

    <div>

        <form method='post' enctype='multipart/form-data'>
            <div>
                <input type='file' name='uploadedFile'>
                <button>Process Text</button>
            </div>
        </form>


        <div class="element">
            <span class="headline">Annotations</span>
            <div id="annotations" class="contentInternal" style="font-size: 12pt;">



            </div>
        </div>

        <div class="element">
        <span class="headline">XMI-View <span style="float:right;"><i class="fa-solid fa-up-down"></i></span></span>
        <div id="xmlviewer" class="contentInternal" style="font-size: 12pt;">



        </div>
        </div>

        <#if result?has_content>
            <script>

                var sXml = "${result?j_string}";

                $("#xmlviewer").simpleXML({
                    xmlString: sXml,
                    collapsedText: "..."
                });

            </script>
        </#if>

    </div>

</div>

</body>

<script>

    $(document).ready(function () {

        $('.headline').click(function () {
            //$('.contentInternal').css("display", "none"); // first hide all the info boxes
            $(this).closest('.element').find('.contentInternal').css("display", "block");
            console.log(this);
            console.log($(this).closest('.element'));
        });
    });

</script>

</html>
